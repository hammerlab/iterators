package org.hammerlab.iterator.sorted

trait StringIntTest
  extends WrappedIntString {
  self: Suite ⇒

  override type L = String
  override type R = WrappedInt

  override implicit def tv = strlen
  override implicit val uv = unwrapWrappedInt

  def expected: Seq[Result]

  test("different types") {
    check(strings)(wrappedInts)(expected)
  }
}
