package org.hammerlab.iterator.start

import hammerlab.iterator.start._
import org.hammerlab.iterator.either.EitherIteratorTest._
import org.hammerlab.Suite

class BufferedCollectWhileTest
  extends Suite {

  def check(elems: Either[Int, String]*)(expectedInts: Int*): Unit = {
    eithers(elems)
      .collectwhile {
        case Left(n) ⇒ n
      }
      .toList should be(
      expectedInts
    )
  }

  test("two") {
    check(
      4, 5, "abc", 6
    )(
      4, 5
    )
  }

  test("none") {
    check(
      "abc", 4, 5, 6
    )(

    )
  }

  test("all") {
    check(
      4, 5, 6
    )(
      4, 5, 6
    )
  }

  test("empty") {
    check(

    )(

    )
  }

}
