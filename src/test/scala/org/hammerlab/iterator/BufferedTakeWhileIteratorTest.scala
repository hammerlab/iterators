package org.hammerlab.iterator

import org.hammerlab.iterator.BufferedTakeWhileIterator._
import org.hammerlab.test.Suite

class BufferedTakeWhileIteratorTest
  extends Suite {
  test("simple") {
    val it = TestIterator(1 to 10: _*).buffered
    it.takewhile(_ < 5).toList should be(1 to 4)
    it.toList should be(5 to 10)
  }

  test("take none") {
    val it = TestIterator(1 to 10: _*).buffered
    it.takewhile(_ < 1).toList should be(Nil)
    it.toList should be(1 to 10)
  }

  test("take all") {
    val it = TestIterator(1 to 10: _*).buffered
    it.takewhile(_ < 11).toList should be(1 to 10)
    it.toList should be(Nil)
  }
}
