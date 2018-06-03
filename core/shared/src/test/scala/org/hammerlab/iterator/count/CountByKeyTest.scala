package org.hammerlab.iterator.count

import hammerlab.iterator.count._
import org.hammerlab.Suite

class CountByKeyTest
  extends Suite {
  test("empty") {
    ==(
      Iterator[(String, Int)]().countByKey
    )(
      Map()
    )
  }

  test("one") {
    ==(
      Iterator(
        "a" → true
      )
      .countByKey,
      Map(
        "a" → 1
      )
    )
  }

  test("two") {
    ==(
      Iterator(
        "a" → true,
        "b" → false
      )
      .countByKey,
      Map(
        "a" → 1,
        "b" → 1
      )
    )
  }

  test("many") {
    ==(
      Iterator(
        "a" → true,
        "b" → false,
        "a" → false,
        "a" → true,
        "c" → false,
        "b" → true,
        "a" → false
      )
      .countByKey,
      Map(
        "a" → 4,
        "b" → 2,
        "c" → 1
      )
    )
  }
}
