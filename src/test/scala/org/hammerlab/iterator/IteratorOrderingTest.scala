package org.hammerlab.iterator

import org.hammerlab.test.Suite

class IteratorOrderingTest extends Suite {
  test("nums") {
    val ord = IteratorOrdering[Int]
    ord.compare(Iterator(), Iterator()) should be(0)
    ord.compare(Iterator(1), Iterator()) should be(1)
    ord.compare(Iterator(), Iterator(1)) should be(-1)
    ord.compare(Iterator(1), Iterator(1)) should be(0)
    ord.compare(Iterator(1, 2), Iterator(1)) should be(1)
    ord.compare(Iterator(1), Iterator(1, 2)) should be(-1)
    ord.compare(Iterator(1, 2), Iterator(1, 2)) should be(0)
    ord.compare(Iterator(1, 1), Iterator(1, 2)) should be(-1)
    ord.compare(Iterator(1, 2), Iterator(1, 1)) should be(1)
  }
}
