package org.hammerlab.iterator.start

import hammerlab.iterator.start._
import org.hammerlab.Suite

class BufferedDropWhileTest
  extends Suite {

  def check(fn: Int ⇒ Boolean,
            expectedRemainingElems: Seq[Int])(
               implicit elems: Seq[Int]
           ): Unit = {
    //Seq(1,2,3).iterator.take(3)
    val it = Iterator(elems: _*).buffered
    it.dropwhile(fn)
    it.toList should be(expectedRemainingElems)
  }

  {
    implicit val elems = 1 to 10

    test("[1,10] half") {
      check(
        _ < 5,
        5 to 10
      )
    }

    test("[1,10] none") {
      check(
        _ < 1,
        1 to 10
      )
    }

    test("[1,10] one") {
      check(
        _ == 1,
        2 to 10
      )
    }

    test("[1,10] almost all") {
      check(
        _ < 10,
        Seq(10)
      )
    }

    test("[1,10] all") {
      check(
        _ < 11,
        Nil
      )
    }
  }

  test("empty") {
    check(
      _ ⇒ true,
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
        Nil
      )
    }

    test("one, none") {
      check(
        _ ⇒ false,
        Seq(1)
      )
    }
  }

}
