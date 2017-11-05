package org.hammerlab.iterator.group

import hammerlab.iterator._
import hammerlab.iterator.macros.IteratorOps

@IteratorOps
class CappedCost[T](it: BufferedIterator[T]) {

  def cappedCostGroup[N: Numeric](withCosts: BufferedIterator[(T, N)], costFn: T ⇒ N, limit: N)(
      implicit elementTooCostlyStrategy: ElementTooCostlyStrategy
  ): Iterator[T] = {

    import ElementTooCostlyStrategy._

    new SimpleIterator[T] {
      val numeric = implicitly[Numeric[N]]
      import numeric._
      var cost = numeric.zero
      var idx = 0
      override protected def _advance: Option[T] =
        if (gt(cost, limit))
          None
        else
          withCosts
            .headOption
            .flatMap {
              case (elem, curCost) ⇒
                def take: Some[T] = {
                  cost += curCost
                  idx += 1
                  withCosts.next
                  Some(elem)
                }
                if (lteq(cost + curCost, limit)) {
                  take
                } else if (idx == 0)
                  elementTooCostlyStrategy match {
                    case Discard ⇒
                      withCosts.next
                      _advance
                    case EmitAlone ⇒
                      take
                    case Throw ⇒
                      throw ElementTooCostlyException(
                        elem,
                        limit,
                        curCost
                      )
                  }
                else
                  None
            }
    }
  }

  def cappedCostGroups[N: Numeric](costFn: T ⇒ N, limit: N)(
      implicit elementTooCostlyStrategy: ElementTooCostlyStrategy
  ): Iterator[Iterator[T]] = {
    val withCosts = it.map(elem ⇒ elem → costFn(elem)).buffered
    new SimpleIterator[Iterator[T]] {
      override protected def _advance: Option[Iterator[T]] = {
        val group = cappedCostGroup(withCosts, costFn, limit)
        if (group.isEmpty)
          None
        else
          Some(group)
      }
    }
  }
}

sealed trait ElementTooCostlyStrategy

case class ElementTooCostlyException[T, N: Numeric](elem: T, limit: N, cost: N)
  extends Exception(
    s"Element $elem's cost $cost exceeds limit $limit"
  )

object ElementTooCostlyStrategy {
  // Throw away elements that are too costly and start groups over on the other side of such elements
  implicit case object Discard extends ElementTooCostlyStrategy

  // Emit elements that exceed the cost limit in groups by themselves
  implicit case object EmitAlone extends ElementTooCostlyStrategy

  // Throw an exception when encountering elements that exceed the per-group cost limit
  implicit case object Throw extends ElementTooCostlyStrategy
}
