package org.hammerlab.iterator

import org.hammerlab.test.Suite
import SliceIterator._

class SliceIteratorTest
  extends Suite {
  test("10") {
    (0 to 9 iterator).sliceOpt(0, 10).toList should be(0 to 9)
    (0 to 9 iterator).sliceOpt(0,  1).toList should be(0 to 0)
    (0 to 9 iterator).sliceOpt(0,  5).toList should be(0 to 4)
    (0 to 9 iterator).sliceOpt(0, 11).toList should be(0 to 9)
    (0 to 9 iterator).sliceOpt(2, 10).toList should be(2 to 9)
    (0 to 9 iterator).sliceOpt(2,  1).toList should be(2 to 2)
    (0 to 9 iterator).sliceOpt(2,  5).toList should be(2 to 6)
    (0 to 9 iterator).sliceOpt(2    ).toList should be(2 to 9)
    (0 to 9 iterator).sliceOpt(2, 11).toList should be(2 to 9)
  }
}
