package org.hammerlab.iterator

import org.hammerlab.test.Suite
import Sliding2OptIterator._

class Sliding2OptIteratorTest extends Suite {
  test("empty") {
    Iterator().sliding2.toList should be(Nil)
  }

  test("one") {
    Iterator(1).sliding2.toSeq should be(Seq((None, 1)))
  }

  test("two") {
    Iterator(1, 2).sliding2.toSeq should be(Seq((None, 1), (Some(1), 2)))
  }

  test("three") {
    Iterator(1, 2, 3).sliding2.toSeq should be(Seq((None, 1), (Some(1), 2), (Some(2), 3)))
  }
}
