package org.hammerlab

package object types {
  implicit class BoolOps(val b: Boolean) extends AnyVal {
    def |[A](a: ⇒ A): Option[A] =
      if (b)
        Some(a)
      else
        None
  }
}
