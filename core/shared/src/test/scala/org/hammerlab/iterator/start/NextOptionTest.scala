package org.hammerlab.iterator.start

import hammerlab.iterator._
import org.hammerlab.Suite

class NextOptionTest extends Suite {
  test("empty") {
    Iterator().nextOption should be(None)
  }

  test("one elem") {
    Iterator(10).nextOption should be(Some(10))
  }

  test("two elems") {
    Iterator(20, 10).nextOption should be(Some(20))
  }

  test("ten elems") {
    Iterator(1 to 10: _*).nextOption should be(Some(1))
  }
}
