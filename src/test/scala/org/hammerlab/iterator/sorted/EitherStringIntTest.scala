package org.hammerlab.iterator.sorted

import org.hammerlab.iterator.sorted.EitherZipIterator._

class EitherStringIntTest
  extends IntStringEitherTest  {

  override type L = String
  override type R = WrappedInt

  override implicit def tv = strlen
  override implicit def uv = unwrapWrappedInt

  test("different types") {
    strings
      .iterator
      .sortedEitherZip[WrappedInt, Int](
        wrappedInts.iterator
      )
      .toList should be(
        Seq[Either[String, WrappedInt]](
          L(""),
          L("a"),
          L("a"),
          R(1),
          L("bb"),
          R(2),
          L("ccc"),
          R(4),
          L("eeeee"),
          L("ffffff"),
          R(7),
          R(10),
          L("kkkkkkkkkkk"),
          L("nnnnnnnnnnnnnn"),
          R(15)
        )
      )
  }
}
