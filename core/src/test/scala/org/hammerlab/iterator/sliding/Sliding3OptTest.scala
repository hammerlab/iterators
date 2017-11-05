package org.hammerlab.iterator.sliding

import hammerlab.iterator.sliding._
import org.hammerlab.test.Suite

class Sliding3OptTest extends Suite {

  def slidingList[T](elems: T*): List[(Option[T], T, Option[T])] =
    Iterator(elems: _*)
      .sliding3Opt
      .toList

  test("empty") {
    slidingList() should be(Nil)
  }

  test("one") {
    slidingList("a") should be(
      List(
        (None, "a", None)
      )
    )
  }

  test("two") {
    slidingList("a", "b") should be(
      List(
        (None, "a", Some("b")),
        (Some("a"), "b", None)
      )
    )
  }

  test("three") {
    slidingList("a", "b", "c") should be(
      List(
        (None, "a", Some("b")),
        (Some("a"), "b", Some("c")),
        (Some("b"), "c", None)
      )
    )
  }

  test("four") {
    slidingList("a", "b", "c", "d") should be(
      List(
        (None, "a", Some("b")),
        (Some("a"), "b", Some("c")),
        (Some("b"), "c", Some("d")),
        (Some("c"), "d", None)
      )
    )
  }

  test("five") {
    slidingList("a", "b", "c", "d", "e") should be(
      List(
        (None, "a", Some("b")),
        (Some("a"), "b", Some("c")),
        (Some("b"), "c", Some("d")),
        (Some("c"), "d", Some("e")),
        (Some("d"), "e", None)
      )
    )
  }
}
