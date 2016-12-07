package org.hammerlab.iterator

import org.hammerlab.test.Suite

class RangeAccruingIteratorTest extends Suite {
  test("empty") {
    new RangeAccruingIterator(Iterator()).toList should be(Nil)
  }

  test("simple") {
    new RangeAccruingIterator(Iterator(2, 4, 5, 7, 8, 9)).toList should be(
      List(
        2 until 3,
        4 until 6,
        7 until 10
      )
    )
  }

  test("one range") {
    new RangeAccruingIterator(Iterator(2, 3, 4, 5, 6, 7, 8, 9)).toList should be(
      List(
        2 until 10
      )
    )
  }

  test("no ranges") {
    new RangeAccruingIterator(Iterator(2, 4, 6, 8)).toList should be(
      List(
        2 until 3,
        4 until 5,
        6 until 7,
        8 until 9
      )
    )
  }
}
