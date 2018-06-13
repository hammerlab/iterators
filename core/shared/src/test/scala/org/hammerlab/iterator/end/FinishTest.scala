package org.hammerlab.iterator.end

import hammerlab.iterator.end._
import org.hammerlab.Suite

class FinishTest
  extends Suite {

  test("empty") {
    var done = false
    def markDone() { done = true }
    val it = Iterator().finish(markDone())

    ==(done, false)
    ==(it.hasNext, false)
    ==(done, true)
  }

  test("one") {
    var done = false
    val it = Iterator(1).finish({ done = true })

    ==(done, false)
    ==(it.hasNext, true)
    ==(done, false)
    ==(it.next, 1)

    ==(done, false)
    ==(it.hasNext, false)
    ==(done, true)
  }

  test("three") {
    var done = false
    val it = Iterator(1, 2, 3).finish({ done = true })

    ==(done, false)
    ==(it.hasNext, true)
    ==(done, false)
    ==(it.next, 1)

    ==(done, false)
    ==(it.hasNext, true)
    ==(done, false)
    ==(it.next, 2)

    ==(done, false)
    ==(it.hasNext, true)
    ==(done, false)
    ==(it.next, 3)

    ==(done, false)
    ==(it.hasNext, false)
    ==(done, true)
  }
}
