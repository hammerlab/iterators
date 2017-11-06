package org.hammerlab.iterator.ordered

import hammerlab.iterator._
import hammerlab.iterator.macros.IteratorOps
import hammerlab.or._
import org.hammerlab.iterator.util.SimpleIterator

@IteratorOps
class OrMerge[T](l: BufferedIterator[T]) {
  def orMerge[U, V](other: Iterable[U])(
      implicit
      tv: View[T, V],
      uv: View[U, V],
      ord: Ordering[V]
  ): SimpleIterator[Or[T, U]] =
    orMerge(other.iterator)

  def orMerge[U, V](other: Iterator[U])(
      implicit
      tv: View[T, V],
      uv: View[U, V],
      ord: Ordering[V]
  ): SimpleIterator[Or[T, U]] = {
    val r = other.buffered
    new SimpleIterator[Or[T, U]] {
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
