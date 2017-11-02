package org.hammerlab.iterator.sorted

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
}

trait IdentityIntConversions
  extends ConvertToInt {
  self: Suite with IntsTest ⇒
  override implicit val tv: L ⇒ V = identity
  override implicit val uv: R ⇒ R = identity
}
