package org.hammerlab.iterator

import org.hammerlab.test.Suite
import Sliding2Iterator._

class Sliding2PadTest
  extends Suite {

  test("empty") {
    Iterator().sliding2(10).toList should be(Nil)
  }

  test("one") {
    Iterator(1).sliding2(10).toSeq should be(Seq(1 → 10))
  }

  test("two") {
    Iterator(1, 2).sliding2(10).toSeq should be(Seq(1 → 2, 2 → 10))
  }

  test("three") {
    Iterator(1, 2, 3).sliding2(10).toSeq should be(Seq(1 → 2, 2 → 3, 3 → 10))
  }
}
