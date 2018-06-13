package org.hammerlab.iterator.start

import hammerlab.iterator._
import hammerlab.Suite

class NextOptionTest extends Suite {
  test("empty") {
    ==(
      Iterator().nextOption
    )(
      None
    )
  }

  test("one elem") {
    ==(Iterator(10).nextOption, Some(10))
  }

  test("two elems") {
    ==(Iterator(20, 10).nextOption, Some(20))
  }

  test("ten elems") {
    ==(Iterator(1 to 10: _*).nextOption, Some(1))
  }
}
