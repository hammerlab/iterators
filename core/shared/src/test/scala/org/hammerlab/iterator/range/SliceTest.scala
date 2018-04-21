package org.hammerlab.iterator.range

import hammerlab.Suite
import hammerlab.iterator.slice._

class SliceTest
  extends Suite {
  test("10") {
    for {
      start ←     0 to 9
        end ← start to 9
    } {
      ===(
        (0 to 9).sliceOpt(start, end),
        start until end iterator
      )
      ===(
        (0 to 9).sliceOpt(Some(start), Some(end)),
        start until end iterator
      )
      ===(
        (0 to 9).sliceOpt(None, end).toList,
        0 until end toList
      )
      ===(
        (0 to 9).sliceOpt(start, None),
        start until 10 iterator
      )
    }
  }
}
