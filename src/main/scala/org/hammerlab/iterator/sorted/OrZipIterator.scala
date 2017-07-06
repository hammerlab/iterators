package org.hammerlab.iterator.sorted

import org.hammerlab.iterator.{ HeadOptionIterator, SimpleBufferedIterator }
import org.hammerlab.types.{ Both, LO, Or, RO }

case class OrZipIterator[T](l: BufferedIterator[T]) {
//  def sortedOrZip[U, V](other: Iterable[U])(
//      implicit
//      tv: T ⇒ V,
//      uv: U ⇒ V,
//      ord: Ordering[V]
//  ): SimpleBufferedIterator[Or[T, U]] =
//    sortedOrZip(other.iterator)(tv, uv, ord)

//  type Aux[U, V0] = CmpType[T, U] { type V = V0 }

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

trait CmpType[T, U] {
  type V
  implicit def tv: T ⇒ V
  implicit def uv: U ⇒ V
  def ord: Ordering[V]
}

object CmpType {

  type Aux[T, U, V0] = CmpType[T, U] { type V = V0 }

  implicit def makeCmp[T, U, V0](
                                    implicit
                                    tv0: T ⇒ V0,
                                    uv0: U ⇒ V0,
                                    ord0: Ordering[V0]
                                ): CmpType.Aux[T, U, V0] =
    new CmpType[T, U] {
      type V = V0
      override implicit def tv: (T) ⇒ V0 = tv0
      override implicit def uv: (U) ⇒ V0 = uv0
      override def ord: Ordering[V0] = ord0
    }
}

object OrZipIterator {
  implicit def makeOrZipIterator[T](it: Iterator[T]): OrZipIterator[T] =
    OrZipIterator(it.buffered)

  implicit def makeOrZipIteratorFromIterable[T](it: Iterable[T]): OrZipIterator[T] =
    OrZipIterator(it.iterator.buffered)
}
