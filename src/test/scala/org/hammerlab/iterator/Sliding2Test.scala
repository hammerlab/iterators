package org.hammerlab.iterator

import org.hammerlab.test.Suite
import Sliding2Iterator._

class Sliding2Test
  extends Suite {

  test("empty") {
    Iterator().sliding2.toList should be(Nil)
  }

  test("one") {
    Iterator(1).sliding2.toSeq should be(Nil)
  }

  test("two") {
    Iterator(1, 2).sliding2.toSeq should be(Seq(1 → 2))
  }

  test("three") {
    Iterator(1, 2, 3).sliding2.toSeq should be(Seq(1 → 2, 2 → 3))
  }
}
