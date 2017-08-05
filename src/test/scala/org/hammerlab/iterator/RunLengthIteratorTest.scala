package org.hammerlab.iterator

import org.hammerlab.iterator.RunLengthIterator._
import org.hammerlab.test.Suite

class RunLengthIteratorTest extends Suite {

  {
    def check[T](elems: T*)(expected: (T, Int)*): Unit = {
      elems.iterator.runLengthEncode().toSeq should be(expected)
    }

    test("empty") {
      check()()
    }

    test("single") {
      check("a")("a" → 1)
    }

    test("one run") {
      check("a", "a", "a")("a" → 3)
    }

    test("two singletons") {
      check("a", "b")("a" → 1, "b" → 1)
    }

    test("run singleton") {
      check("a", "a", "a", "b")("a" → 3, "b" → 1)
    }

    test("singleton run") {
      check("a", "b", "b", "b")("a" → 1, "b" → 3)
    }

    test("single run single") {
      check("z", "y", "y", "x")("z" → 1, "y" → 2, "x" → 1)
    }

    test("two runs") {
      check("a", "a", "a", "b", "b", "b", "b")("a" → 3, "b" → 4)
    }
  }

  {
    val parityOrdering =
      Ordering.by[Int, Int](_ % 2)

    def check(elems: Int*)(expected: (Int, Int)*): Unit = {
      elems.iterator.runLengthEncode(parityOrdering).toSeq should be(expected)
    }

    test("evens and odds") {
      check(
        1, 5, 3, 7,
        2,
        5, 5,
        4, 6, 8,
        1,
        2
      )(
        1 → 4,
        2 → 1,
        5 → 2,
        4 → 3,
        1 → 1,
        2 → 1
      )
    }
  }

  {
    test("re-encode") {
      reencode(
        Iterator('a' → 2, 'a' → 1, 'b' → 3, 'a' → 4, 'c' → 1, 'c' → 1, 'c' → 2, 'a' → 1)
      )
      .toList should be(
        List(
          'a' → 3,
          'b' → 3,
          'a' → 4,
          'c' → 4,
          'a' → 1
        )
      )
    }
  }

}
