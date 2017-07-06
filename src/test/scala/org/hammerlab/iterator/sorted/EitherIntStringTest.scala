package org.hammerlab.iterator.sorted

import org.hammerlab.iterator.sorted.EitherZipIterator._

class EitherIntStringTest
  extends IntStringEitherTest {

  override type L = WrappedInt
  override type R = String

  override implicit def tv = unwrapWrappedInt
  override implicit def uv = strlen

  test("different types") {
    wrappedInts
      .iterator
      .sortedEitherZip[String, Int](
        strings.iterator
      )
      .toList should be(
        Seq[Either[WrappedInt, String]](
          R(""),
          L(1),
          R("a"),
          R("a"),
          L(2),
          R("bb"),
          R("ccc"),
          L(4),
          R("eeeee"),
          R("ffffff"),
          L(7),
          L(10),
          R("kkkkkkkkkkk"),
          R("nnnnnnnnnnnnnn"),
          L(15)
        )
      )
  }
}
