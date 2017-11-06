package org.hammerlab.iterator.end

import hammerlab.iterator.end._
import org.hammerlab.test.Suite

class FinishTest
  extends Suite {

  test("empty") {
    var done = false
    def markDone() { done = true }
    val it = Iterator().finish(markDone())

    done should be(false)
    it.hasNext should be(false)
    done should be(true)
  }

  test("one") {
    var done = false
    val it = Iterator(1).finish({ done = true })

    done should be(false)
    it.hasNext should be(true)
    done should be(false)
    it.next should be(1)

    done should be(false)
    it.hasNext should be(false)
    done should be(true)
  }

  test("three") {
    var done = false
    val it = Iterator(1, 2, 3).finish({ done = true })

    done should be(false)
    it.hasNext should be(true)
    done should be(false)
    it.next should be(1)

    done should be(false)
    it.hasNext should be(true)
    done should be(false)
    it.next should be(2)

    done should be(false)
    it.hasNext should be(true)
    done should be(false)
    it.next should be(3)

    done should be(false)
    it.hasNext should be(false)
    done should be(true)
  }
}
