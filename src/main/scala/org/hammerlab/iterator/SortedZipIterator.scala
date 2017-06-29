package org.hammerlab.iterator

import shapeless.Lazy

case class SortedZipIterator[T](l: BufferedIterator[T]) {

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

  def sortedEitherZip[U, V](other: Iterator[U])(
      implicit
      tv: T ⇒ V,
      uv: U ⇒ V,
      ord: Ordering[V]
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

object SortedZipIterator {
  implicit def makeSortedZipIterator[T](it: Iterator[T]): SortedZipIterator[T] =
    SortedZipIterator(it.buffered)
}
