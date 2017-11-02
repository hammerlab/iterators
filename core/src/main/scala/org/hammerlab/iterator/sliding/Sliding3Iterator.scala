package org.hammerlab.iterator.sliding

import org.hammerlab.iterator.{ NextOptionIterator, SimpleBufferedIterator }

/**
 * Given an [[Iterator[T]]], emit each element sandwiched between its preceding and succeeding elements.
 */
case class Sliding3Iterator[T](it: BufferedIterator[T]) {

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

object Sliding3Iterator {
  implicit def makeSliding3Iterator[T](it: Iterator[T]): Sliding3Iterator[T] = Sliding3Iterator(it.buffered)
  implicit def makeSliding3Iterator[T](it: Iterable[T]): Sliding3Iterator[T] = Sliding3Iterator(it.iterator.buffered)
}
