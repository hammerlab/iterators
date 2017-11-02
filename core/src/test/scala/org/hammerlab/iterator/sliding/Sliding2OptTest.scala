package org.hammerlab.iterator.sliding

import org.hammerlab.iterator.sliding.Sliding2Iterator._
import org.hammerlab.test.Suite

class Sliding2OptTest extends Suite {
  test("empty") {
    Iterator().sliding2Opt.toList should be(Nil)
  }

  test("one") {
    Iterator(1).sliding2Opt.toSeq should be(Seq(1 → None))
  }

  test("two") {
    Iterator(1, 2).sliding2Opt.toSeq should be(Seq(1 → Some(2), 2 → None))
  }

  test("three") {
    Iterator(1, 2, 3).sliding2Opt.toSeq should be(Seq(1 → Some(2), 2 → Some(3), 3 → None))
  }
}
