package org.hammerlab.iterator

import org.hammerlab.iterator.CountIteratorByKey._
import org.hammerlab.test.Suite

class CountIteratorByKeyTest
  extends Suite {
  test("empty") {
    Iterator[(String, Int)]().countByKey should be(Map())
  }

  test("one") {
    Iterator(
      "a" → true
    )
    .countByKey should be(
      Map(
        "a" → 1
      )
    )
  }

  test("two") {
    Iterator(
      "a" → true,
      "b" → false
    )
    .countByKey should be(
      Map(
        "a" → 1,
        "b" → 1
      )
    )
  }

  test("many") {
    Iterator(
      "a" → true,
      "b" → false,
      "a" → false,
      "a" → true,
      "c" → false,
      "b" → true,
      "a" → false
    )
    .countByKey should be(
      Map(
        "a" → 4,
        "b" → 2,
        "c" → 1
      )
    )
  }
}
