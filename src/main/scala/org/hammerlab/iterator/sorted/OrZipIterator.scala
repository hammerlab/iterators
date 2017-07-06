package org.hammerlab.iterator.sorted

import org.hammerlab.iterator.{ HeadOptionIterator, SimpleBufferedIterator }
import org.hammerlab.types.{ Both, LO, Or, RO }

case class OrZipIterator[T](l: BufferedIterator[T]) {
  def sortedOrZip[U, V](other: Iterable[U])(
      implicit
      ord: Ordering[V],
      tv: T ⇒ V,
      uv: U ⇒ V
  ): SimpleBufferedIterator[Or[T, U]] =
    sortedOrZip(other.iterator)

  def sortedOrZip[U, V](other: Iterator[U])(
      implicit
      ord: Ordering[V],
      tv: T ⇒ V,
      uv: U ⇒ V
  ): SimpleBufferedIterator[Or[T, U]] = {
    val r = other.buffered
    new SimpleBufferedIterator[Or[T, U]] {
      override protected def _advance: Option[Or[T, U]] =
        (l.headOption, r.headOption) match {
          case (Some(t), Some(u)) ⇒
            Some(
              ord.compare(t, u) match {
                case 0 ⇒
                  l.next
                  r.next
                  Both(t, u)
                case x if x < 0 ⇒
                  l.next
                  LO(t)
                case _ ⇒
                  r.next
                  RO(u)
              }
            )
          case (Some(t), _) ⇒
            l.next
            Some(LO(t))
          case (_, Some(u)) ⇒
            r.next
            Some(RO(u))
          case _ ⇒
            None
        }
    }
  }
}

object OrZipIterator {
  implicit def makeOrZipIterator[T](it: Iterator[T]): OrZipIterator[T] =
    OrZipIterator(it.buffered)

  implicit def makeOrZipIteratorFromIterable[T](it: Iterable[T]): OrZipIterator[T] =
    OrZipIterator(it.iterator.buffered)
}
