package org.hammerlab.iterator.group

import hammerlab.iterator.group._
import org.hammerlab.Suite

class SplitTest extends Suite {
  test("simple") {
    "abc defg hij".iterator.splitBy(' ').map(_.mkString("")).toList should be(
      List(
        "abc",
        "bc",
        "c",
        "",
        "defg",
        "efg",
        "fg",
        "g",
        "",
        "hij",
        "ij",
        "j"
      )
    )
  }

  test("double-space and trailing space") {
    "abc defg  hij ".toIterator.splitBy(' ').map(_.mkString("")).toList should be(
      List(
        "abc",
        "bc",
        "c",
        "",
        "defg",
        "efg",
        "fg",
        "g",
        "",
        "",
        "hij",
        "ij",
        "j",
        ""
      )
    )
  }

  test("leading spaces, multiple-space, trailing spaces") {
    "  abc defg    hij   ".toIterator.splitBy(' ').map(_.mkString("")).toList should be(
      List(
        "",
        "",
        "abc",
        "bc",
        "c",
        "",
        "defg",
        "efg",
        "fg",
        "g",
        "",
        "",
        "",
        "",
        "hij",
        "ij",
        "j",
        "",
        "",
        ""
      )
    )
  }
}
