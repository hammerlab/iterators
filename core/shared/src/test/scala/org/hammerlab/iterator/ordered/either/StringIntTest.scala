package org.hammerlab.iterator.ordered.either

import org.hammerlab.iterator.ordered

class StringIntTest
  extends Suite
    with ordered.StringIntTest {
  override def expected: Seq[Either[L, R]] =
    Seq(
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
      R(11),
      R(11),
      L("nnnnnnnnnnnnnn"),
      R(15)
    )
}
