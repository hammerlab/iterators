package org.hammerlab.iterator.sliding

import hammerlab.iterator.sliding._
import org.hammerlab.Suite

class Sliding2OptTest extends Suite {
  test("empty") {
    ==(Iterator().sliding2Opt.toList, Nil)
  }

  test("one") {
    ==(Iterator(1).sliding2Opt.toSeq, Seq(1 → None))
  }

  test("two") {
    ==(Iterator(1, 2).sliding2Opt.toSeq, Seq(1 → Some(2), 2 → None))
  }

  test("three") {
    ==(Iterator(1, 2, 3).sliding2Opt.toSeq, Seq(1 → Some(2), 2 → Some(3), 3 → None))
  }
}
