package org.hammerlab.iterator.sorted

import hammerlab.either._
import hammerlab.iterator._
import hammerlab.iterator.macros.IteratorWrapper
import org.hammerlab.iterator.SimpleBufferedIterator

@IteratorWrapper
class OrZip[T](l: BufferedIterator[T]) {
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
                  L(t)
                case _ ⇒
                  r.next
                  R(u)
              }
            )
          case (Some(t), _) ⇒
            l.next
            Some(L(t))
          case (_, Some(u)) ⇒
            r.next
            Some(R(u))
          case _ ⇒
            None
        }
    }
  }
}
