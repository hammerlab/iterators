package org.hammerlab.iterator

import HeadOptionIterator._

/**
 * Given an [[Iterator[T]]], emit each element sandwiched between its preceding and succeeding elements.
 */
class Sliding3OptIterator[T](it: BufferedIterator[T]) {

  def sliding3: Iterator[(T, T, T)] =
    sliding3Opt
      .flatMap {
        case (Some(prev), cur, Some(next)) ⇒
          Some((prev, cur, next))
        case _ ⇒
          None
      }

  def sliding3Opt: Iterator[(Option[T], T, Option[T])] =
    new SimpleBufferedIterator[(Option[T], T, Option[T])] {
      private var lastOpt: Option[T] = None

      override protected def _advance: Option[(Option[T], T, Option[T])] = {
        if (!it.hasNext)
          return None

        val cur = it.next()

        val prevOpt = lastOpt
        lastOpt = Some(cur)

        val nextOpt =
          if (it.hasNext)
            Some(it.head)
          else
            None

        Some(
          (
            prevOpt,
            cur,
            nextOpt
          )
        )
      }
    }

  def sliding3NextOpts: Iterator[(T, Option[T], Option[T])] =
    new SimpleBufferedIterator[(T, Option[T], Option[T])] {
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

object Sliding3OptIterator {
  implicit def make[T](it: Iterator[T]): Sliding3OptIterator[T] = new Sliding3OptIterator(it.buffered)
  implicit def make[T](it: Iterable[T]): Sliding3OptIterator[T] = new Sliding3OptIterator(it.iterator.buffered)
}
