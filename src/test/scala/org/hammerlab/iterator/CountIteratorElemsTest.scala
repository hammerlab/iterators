package org.hammerlab.iterator

import org.hammerlab.iterator.CountIteratorElems._
import org.hammerlab.test.Suite

class CountIteratorElemsTest
  extends Suite {
  test("empty") {
    Iterator().countElems should be(Map())
  }

  test("one") {
    Iterator(
      "a"
    )
    .countElems should be(
      Map(
        "a" → 1
      )
    )
  }

  test("two") {
    Iterator(
      "a",
      "b"
    )
    .countElems should be(
      Map(
        "a" → 1,
        "b" → 1
      )
    )
  }

  test("many") {
    Iterator(
      "a",
      "b",
      "a",
      "a",
      "c",
      "b",
      "a"
    )
    .countElems should be(
      Map(
        "a" → 4,
        "b" → 2,
        "c" → 1
      )
    )
  }
}
