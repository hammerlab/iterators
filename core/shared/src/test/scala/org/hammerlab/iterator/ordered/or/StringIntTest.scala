package org.hammerlab.iterator.ordered.or

import org.hammerlab.iterator.ordered

class StringIntTest
  extends Suite
    with ordered.StringIntTest {

  override def expected: Seq[Result] =
    Seq(
      L(""),
      B("a", 1),
      L("a"),
      B("bb", 2),
      L("ccc"),
      R(4),
      L("eeeee"),
      L("ffffff"),
      R(7),
      R(10),
      B("kkkkkkkkkkk", 11),
      R(11),
      L("nnnnnnnnnnnnnn"),
      R(15)
    )
}
