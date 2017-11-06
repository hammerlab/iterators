package org.hammerlab.iterator.ordered

import hammerlab.iterator._
import hammerlab.iterator.macros.IteratorOps
import org.hammerlab.iterator.util.SimpleIterator

@IteratorOps
class EitherMerge[T](l: BufferedIterator[T]) {
  def eitherMerge[U, V](other: Iterable[U])(
      implicit
      tv: View[T, V],
      uv: View[U, V],
      ord: Ordering[V]
  ): SimpleIterator[Either[T, U]] =
    eitherMerge(other.iterator)

  def eitherMerge[U, V](other: Iterator[U])(
      implicit
      tv: View[T, V],
      uv: View[U, V],
      ord: Ordering[V]
  ): SimpleIterator[Either[T, U]] = {
    val r = other.buffered
    val ≤ = ord.lteq _
    new SimpleIterator[Either[T, U]] {
      override protected def _advance: Option[Either[T, U]] =
        (l.headOption, r.headOption) match {
          case (Some(t), Some(u)) ⇒
            if (≤(tv(t), uv(u))) {
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
