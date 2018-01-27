package org.hammerlab.iterator.range

import hammerlab.iterator.range._
import org.hammerlab.Suite

class ContiguousTest extends Suite {
  test("empty") {
    Iterator().contiguousRanges.toList should be(Nil)
  }

  test("simple") {
    Iterator(2, 4, 5, 7, 8, 9).contiguousRanges.toList should be(
      List(
        2 until 3,
        4 until 6,
        7 until 10
      )
    )
  }

  test("one range") {
    Iterator(2, 3, 4, 5, 6, 7, 8, 9).contiguousRanges.toList should be(
      List(
        2 until 10
      )
    )
  }

  test("no ranges") {
    Iterator(2, 4, 6, 8).contiguousRanges.toList should be(
      List(
        2 until 3,
        4 until 5,
        6 until 7,
        8 until 9
      )
    )
  }
}
