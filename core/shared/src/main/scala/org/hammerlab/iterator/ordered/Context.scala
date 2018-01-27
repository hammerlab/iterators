package org.hammerlab.iterator.ordered

/**
 * Handy wrapper for implicits reused across [[EitherMerge]], [[OrMerge]], [[LeftMerge]]
 */
case class Context[T, U, V](implicit
                            val tv: T ⇒ V,
                            val uv: U ⇒ V,
                            val ord: Ordering[V])

object Context {
  implicit def make[T, U, V](implicit
                             tv: View[T, V],
                             uv: View[U, V],
                             ord: Ordering[V]): Context[T, U, V] =
    Context()
}
