package org.hammerlab.iterator.sliding

import hammerlab.iterator._
import hammerlab.iterator.macros.IteratorOps
import org.hammerlab.iterator.SimpleIterator

/**
 * Given an [[Iterator[T]]], emit each element sandwiched between its preceding and succeeding elements.
 */
@IteratorOps
class Sliding3[T](it: BufferedIterator[T]) {

  def sliding3: Iterator[(T, T, T)] =
    sliding3Opt
      .flatMap {
        case (Some(prev), cur, Some(next)) ⇒
          Some((prev, cur, next))
        case _ ⇒
          None
      }

  def sliding3Opt: Iterator[(Option[T], T, Option[T])] =
    new SimpleIterator[(Option[T], T, Option[T])] {
      private var lastOpt: Option[T] = None

      override protected def _advance: Option[(Option[T], T, Option[T])] =
        it
          .nextOption
          .map {
            cur ⇒

              val prevOpt = lastOpt
              lastOpt = Some(cur)

              val nextOpt =
                if (it.hasNext)
                  Some(it.head)
                else
                  None

              (
                prevOpt,
                cur,
                nextOpt
              )
          }
    }

  def sliding3NextOpts: Iterator[(T, Option[T], Option[T])] =
    new SimpleIterator[(T, Option[T], Option[T])] {
      var prevOpt: Option[(T, Option[T])] = None
      override protected def _advance: Option[(T, Option[T], Option[T])] =
        prevOpt match {
          case None ⇒
            if (it.hasNext) {
              prevOpt = Some(it.next → it.nextOption)
              _advance
            } else
              None
          case Some((cur, nxtOpt)) ⇒
            val nextOption = it.nextOption
            val ret = (cur, nxtOpt, nextOption)
            prevOpt = nxtOpt.map(nxt ⇒ nxt → nextOption)
            Some(ret)
        }
    }
}
