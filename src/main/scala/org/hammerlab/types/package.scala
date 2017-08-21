package org.hammerlab

import cats.data.Ior

package object types {
  implicit class BoolOps(val b: Boolean) extends AnyVal {
    def |[A](a: ⇒ A): Option[A] =
      if (b)
        Some(a)
      else
        None
  }

  /** Syntactic sugar around [[cats.data.Ior]] */
  type Or[+A, +B] = Ior[A, B]

  object L {
    def apply[A](a: A) = Ior.Left(a)
    def unapply[A](l: Ior.Left[A]): Option[A] = Some(l.a)
  }

  object R {
    def apply[B](b: B) = Ior.Right(b)
    def unapply[B](r: Ior.Right[B]): Option[B] = Some(r.b)
  }

  object Both {
    def apply[A, B](a: A, b: B) = Ior.Both(a, b)
    def unapply[A, B](b: Ior.Both[A, B]): Option[(A, B)] = Some((b.a, b.b))
  }
}
