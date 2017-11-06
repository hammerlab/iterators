package org.hammerlab.iterator.ordered

import hammerlab.iterator._
import hammerlab.iterator.macros.IteratorOps
import org.hammerlab.iterator.util.SimpleIterator

@IteratorOps
class Merge[T](l: BufferedIterator[T]) {
  def merge[V](other: Iterable[T])(
      implicit
      ord: Ordering[V],
      tv: T ⇒ V
  ): SimpleIterator[T] =
    merge[V](other.iterator)

  def merge[V](other: Iterator[T])(
      implicit
      ord: Ordering[V],
      tv: T ⇒ V
  ): SimpleIterator[T] = {
    val r = other.buffered
    val ≤ = ord.lteq _
    new SimpleIterator[T] {
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
