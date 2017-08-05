package org.hammerlab.iterator.bulk

import org.hammerlab.iterator.bulk.BufferedBulkIterator._
import org.hammerlab.test.Suite

class BufferedTakeWhileTest
  extends Suite {

  def check(fn: Int ⇒ Boolean,
            expectedElems: Seq[Int],
            expectedRemainingElems: Seq[Int])(
      implicit elems: Seq[Int]
  ): Unit = {
    val it = Iterator(elems: _*).buffered
    it.takewhile(fn).toList should be(expectedElems)
    it.toList should be(expectedRemainingElems)
  }

  {
    implicit val elems = 1 to 10

    test("[1,10] half") {
      check(
        _ < 5,
        1 to 4,
        5 to 10
      )
    }

    test("[1,10] none") {
      check(
        _ < 1,
        Nil,
        1 to 10
      )
    }

    test("[1,10] one") {
      check(
        _ == 1,
        Seq(1),
        2 to 10
      )
    }

    test("[1,10] almost all") {
      check(
        _ < 10,
        1 to 9,
        Seq(10)
      )
    }

    test("[1,10] all") {
      check(
        _ < 11,
        1 to 10,
        Nil
      )
    }
  }

  test("empty") {
    check(
      _ ⇒ true,
      Nil,
      Nil
    )(
      Nil
    )
  }

  {
    implicit val elems = Seq(1)

    test("one, all") {
      check(
        _ ⇒ true,
        Seq(1),
        Nil
      )
    }

    test("one, none") {
      check(
        _ ⇒ false,
        Nil,
        Seq(1)
      )
    }
  }
}
