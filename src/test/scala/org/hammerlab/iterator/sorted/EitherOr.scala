package org.hammerlab.iterator.sorted

trait EitherOr {
  self: ZipIteratorTest ⇒

  type V = Int

  implicit def tv: L ⇒ V
  implicit def uv: R ⇒ V
  implicit def ord: Ordering[V] = Ordering.Int
}
