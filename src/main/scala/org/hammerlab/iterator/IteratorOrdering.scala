package org.hammerlab.iterator

object IteratorOrdering {
  implicit def apply[T](implicit ord: Ordering[T]): Ordering[Iterator[T]] =
    new Ordering[Iterator[T]] {
      override def compare(x: Iterator[T], y: Iterator[T]): Int =
        (x.hasNext, y.hasNext) match {
          case (false, false) ⇒ 0
          case (true, false) ⇒ 1
          case (false, true) ⇒ -1
          case _ ⇒
            ord.compare(x.next, y.next) match {
              case 0 ⇒ compare(x, y)
              case x ⇒ x
            }
        }
    }
}
