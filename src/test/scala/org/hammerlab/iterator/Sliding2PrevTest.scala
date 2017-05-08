package org.hammerlab.iterator

import org.hammerlab.test.Suite
import Sliding2Iterator._

class Sliding2PrevTest extends Suite {
  test("empty") {
    Iterator().sliding2Prev.toList should be(Nil)
  }

  test("one") {
    Iterator(1).sliding2Prev.toSeq should be(Seq(None → 1))
  }

  test("two") {
    Iterator(1, 2).sliding2Prev.toSeq should be(Seq(None → 1, Some(1) → 2))
  }

  test("three") {
    Iterator(1, 2, 3).sliding2Prev.toSeq should be(Seq(None → 1, Some(1) → 2, Some(2) → 3))
  }
}
