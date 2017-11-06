package org.hammerlab.iterator.ordered

trait VInt {
  self: Suite ⇒
  type V = Int
  implicit def ord: Ordering[V] = Ordering.Int
}

trait ConvertToInt
  extends VInt {
  self: Suite ⇒
  implicit def tv: L ⇒ V
  implicit def uv: R ⇒ V

  implicit def lv: View[L, V] = View(tv)
  implicit def rv: View[R, V] = View(uv)
}

trait IdentityIntConversions
  extends ConvertToInt {
  self: Suite with IntsTest ⇒
  override implicit val tv: L ⇒ V = identity
  override implicit val uv: R ⇒ R = identity
}
