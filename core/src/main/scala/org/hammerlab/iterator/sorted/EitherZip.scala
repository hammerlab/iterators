package org.hammerlab.iterator.sorted

import hammerlab.iterator.macros.IteratorWrapper
import org.hammerlab.iterator.{ HeadOptionIterator, SimpleBufferedIterator }

@IteratorWrapper
class EitherZip[T](l: BufferedIterator[T]) {
  def sortedEitherZip[U, V](other: Iterable[U])(
      implicit
      ord: Ordering[V],
      tv: T ⇒ V,
      uv: U ⇒ V
  ): SimpleBufferedIterator[Either[T, U]] =
    sortedEitherZip(other.iterator)

  def sortedEitherZip[U, V](other: Iterator[U])(
      implicit
      ord: Ordering[V],
      tv: T ⇒ V,
      uv: U ⇒ V
  ): SimpleBufferedIterator[Either[T, U]] = {
    val r = other.buffered
    val ≤ = ord.lteq _
    new SimpleBufferedIterator[Either[T, U]] {
      override protected def _advance: Option[Either[T, U]] =
        (l.headOption, r.headOption) match {
          case (Some(t), Some(u)) ⇒
            if (≤(t, u)) {
              l.next
              Some(Left(t))
            } else {
              r.next
              Some(Right(u))
            }
          case (Some(t), _) ⇒
            l.next
            Some(Left(t))
          case (_, Some(u)) ⇒
            r.next
            Some(Right(u))
          case _ ⇒
            None
        }
    }
  }
}
