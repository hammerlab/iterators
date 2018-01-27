package org.hammerlab.iterator.ordered

trait IntStringTest
  extends WrappedIntString {
  self: Suite ⇒

  override type L = WrappedInt
  override type R = String

  override implicit def tv = unwrapWrappedInt
  override implicit val uv = strlen _

  def expected: Seq[Result]

  test("different types") {
    check(wrappedInts)(strings)(expected)
  }
}
