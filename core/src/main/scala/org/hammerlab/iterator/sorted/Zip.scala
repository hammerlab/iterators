package org.hammerlab.iterator.sorted

import hammerlab.iterator.macros.IteratorWrapper
import org.hammerlab.iterator.{ HeadOptionIterator, SimpleBufferedIterator }

@IteratorWrapper
class Zip[T](l: BufferedIterator[T]) {
  def sortedZip[V](other: Iterable[T])(
      implicit
      ord: Ordering[V],
      tv: T ⇒ V
  ): SimpleBufferedIterator[T] =
    sortedZip[V](other.iterator)

  def sortedZip[V](other: Iterator[T])(
      implicit
      ord: Ordering[V],
      tv: T ⇒ V
  ): SimpleBufferedIterator[T] = {
    val r = other.buffered
    val ≤ = ord.lteq _
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
