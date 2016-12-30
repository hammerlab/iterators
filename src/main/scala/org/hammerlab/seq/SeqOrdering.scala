package org.hammerlab.seq

import org.hammerlab.iterator.IteratorOrdering

object SeqOrdering {
  implicit def apply[T](implicit ord: Ordering[T]): Ordering[Seq[T]] =
    new Ordering[Seq[T]] {
      override def compare(x: Seq[T], y: Seq[T]): Int =
        IteratorOrdering(ord).compare(x.iterator, y.iterator)
    }
}
