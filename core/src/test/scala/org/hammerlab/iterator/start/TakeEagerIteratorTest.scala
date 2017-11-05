package org.hammerlab.iterator.start

import hammerlab.iterator.start._
import org.hammerlab.test.Suite

class TakeEagerIteratorTest
  extends Suite {

  test("empty") {
    Iterator().takeEager(2) should be(Array())
  }

  test("take none") {
    (1 to 10).iterator.takeEager(0) should be(Array())
  }

  test("10-2") {
    (1 to 10).iterator.takeEager(2) should be(Array(1, 2))
  }

  test("10-20") {
    (1 to 10).iterator.takeEager(20) should be(1 to 10)
  }
}
