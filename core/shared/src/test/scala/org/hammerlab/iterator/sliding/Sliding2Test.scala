package org.hammerlab.iterator.sliding

import hammerlab.iterator.sliding._
import org.hammerlab.Suite

class Sliding2Test
  extends Suite {

  test("empty") {
    ==(Iterator().sliding2.toList, Nil)
  }

  test("one") {
    ==(Iterator(1).sliding2.toSeq, Nil)
  }

  test("two") {
    ==(Iterator(1, 2).sliding2.toSeq, Seq(1 → 2))
  }

  test("three") {
    ==(Iterator(1, 2, 3).sliding2.toSeq, Seq(1 → 2, 2 → 3))
  }
}
