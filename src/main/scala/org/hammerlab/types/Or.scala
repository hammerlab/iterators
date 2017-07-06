package org.hammerlab.types

sealed trait Or[+L, +R]

object Or {
  def apply[L, R](l: L, r: Option[R]): Or[L, R] =
    r match {
      case Some(r) ⇒ Both(l, r)
      case None ⇒ LO(l)
    }

  def apply[L, R](l: Option[L], r: R): Or[L, R] =
    l match {
      case Some(l) ⇒ Both(l, r)
      case None ⇒ RO(r)
    }
}

final case class LO[+L, +R](l: L) extends Or[L, R]
final case class RO[+L, +R](r: R) extends Or[L, R]
final case class Both[+L, +R](l: L, r: R) extends Or[L, R]
