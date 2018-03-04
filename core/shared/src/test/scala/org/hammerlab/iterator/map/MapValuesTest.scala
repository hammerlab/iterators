package org.hammerlab.iterator.map

import hammerlab.iterator._
import org.hammerlab.Suite

class MapValuesTest
  extends Suite {
  test("empty") {
    Iterator[(Char, Int)]().mapValues(-_).toList should be(Nil)
  }

  test("list") {
    List('a → 1, 'b → 2).mapValues(-_).toList should be(List('a → -1, 'b → -2))
  }

  test("array") {
    Array('a → 1, 'b → 2, 'a → 3).mapValues(-_).toList should be(List('a → -1, 'b → -2, 'a → -3))
  }
}
