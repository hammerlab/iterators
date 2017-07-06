package org.hammerlab.iterator.sorted

import org.hammerlab.iterator.{ HeadOptionIterator, SimpleBufferedIterator }

case class ZipIterator[T](l: BufferedIterator[T]) {
  def sortedZip[V: Ordering](other: Iterable[T])(
      implicit
      tv: T ⇒ V
  ): SimpleBufferedIterator[T] =
    sortedZip[V](other)

  def sortedZip[V: Ordering](other: Iterator[T])(
      implicit
      tv: T ⇒ V
  ): SimpleBufferedIterator[T] = {
    val r = other.buffered
    val ≤ = implicitly[Ordering[V]].lteq _
    new SimpleBufferedIterator[T] {
      override protected def _advance: Option[T] =
        (l.headOption, r.headOption) match {
          case (Some(t), Some(u)) ⇒
            if (≤(t, u)) {
              l.next
              Some(t)
            } else {
              r.next
              Some(u)
            }
          case (Some(t), _) ⇒
            l.next
            Some(t)
          case (_, Some(u)) ⇒
            r.next
            Some(u)
          case _ ⇒
            None
        }
    }
  }
}

object ZipIterator {
  implicit def makeZipIterator[T](it: Iterator[T]): ZipIterator[T] =
    ZipIterator(it.buffered)

  implicit def makeZipIteratorFromIterable[T](it: Iterable[T]): ZipIterator[T] =
    ZipIterator(it.iterator.buffered)
}
