package org.hammerlab.iterator.group

import cats.Eq

trait Cmp[T]
  extends Serializable {
  def apply(l: T, r: T): Boolean
}

trait LowPriCmp
  extends Serializable {
  def apply[T](fn: (T, T) ⇒ Boolean, str: String): Cmp[T] =
    new Cmp[T] {
      override def toString: String = str
      @inline override def apply(l: T, r: T): Boolean = fn(l, r)
    }
  implicit def fromOrd[T](implicit o: Ordering[T]): Cmp[T] = Cmp(o.equiv, "fromOrd")
}

object Cmp
  extends LowPriCmp {
  implicit def fromEq[T](implicit e: Eq[T]): Cmp[T] = Cmp(e.eqv, "fromEq")
}
