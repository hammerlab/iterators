package org.hammerlab.iterator

import org.hammerlab.test.Suite

class TakeUntilIteratorTest extends Suite {
  test("simple") {
    new TakeUntilIterator("abc defg hij".toIterator, ' ').map(_.mkString("")).toList should be(
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
    new TakeUntilIterator("abc defg  hij ".toIterator, ' ').map(_.mkString("")).toList should be(
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
    new TakeUntilIterator("  abc defg    hij   ".toIterator, ' ').map(_.mkString("")).toList should be(
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
