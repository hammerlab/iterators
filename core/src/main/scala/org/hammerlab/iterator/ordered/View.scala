package org.hammerlab.iterator.ordered

/**
 * Simple type-class corresponding to the ability to convert from [[From]] to [[To]]
 *
 * Insulates against stdlib implicit conversions causing diverging-implicit errors.
 */
trait View[-From, +To]
  extends (From ⇒ To)
    with Serializable

trait LowPriView {
  def apply[T, U](fn: T ⇒ U): View[T, U] =
    new View[T, U] {
      override def apply(from: T) = fn(from)
    }

  implicit def fromConv[T, U](implicit fn: T ⇒ U): View[T, U] = apply(fn)
}

object View
  extends LowPriView {
  implicit def id[T]: View[T, T] = apply(identity)
}
