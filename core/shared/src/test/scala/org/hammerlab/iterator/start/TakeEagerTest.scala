package org.hammerlab.iterator.start

import hammerlab.iterator.start._
import org.hammerlab.Suite

class TakeEagerTest
  extends Suite {

  test("empty") {
    ==(
      Iterator().takeEager(2)
    )(
      Array()
    )
  }

  test("take none") {
    ==(
      (1 to 10).iterator.takeEager(0)
    )(
      Array()
    )
  }

  test("10-2") {
    ==((1 to 10).iterator.takeEager(2), Array(1, 2))
  }

  test("10-20") {
    ==((1 to 10).iterator.takeEager(20), 1 to 10 toArray)
  }
}
