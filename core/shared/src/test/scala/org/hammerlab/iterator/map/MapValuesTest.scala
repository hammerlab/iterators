package org.hammerlab.iterator.map

import hammerlab.iterator._
import org.hammerlab.Suite
import org.hammerlab.test.Cmp

class MapValuesTest
  extends Suite {
  test("empty") {
    ==(
      Iterator[(Char, Int)]()
        .mapValues(-_)
        .toList,
      Nil
    )
  }

  test("list") {
    ==(
      List(
        'a → 1,
        'b → 2
      )
      .mapValues(-_)
      .toList,
      List(
        'a → -1,
        'b → -2
      )
    )
  }

  test("array") {
    implicitly[Cmp[Symbol]]
    implicitly[Cmp[(Symbol, Int)]]
    ==(
      Array(
        'a → 1,
        'b → 2,
        'a → 3
      )
      .mapValues(-_)
      .toList,
      List(
        'a → -1,
        'b → -2,
        'a → -3
      )
    )
  }
}
