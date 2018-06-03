package org.hammerlab.iterator.sliding

import hammerlab.iterator.sliding._
import org.hammerlab.Suite

class Sliding3NextOptsTest extends Suite {

  def slidingList[T](elems: T*): List[(T, Option[T], Option[T])] =
    Iterator(elems: _*)
      .sliding3NextOpts
      .toList

  test("empty") {
    ==(slidingList(), Nil)
  }

  test("one") {
    ==(
      slidingList("a"),
      List(
        ("a", None, None)
      )
    )
  }

  test("two") {
    ==(
      slidingList("a", "b"),
      List(
        ("a", Some("b"), None),
        ("b", None, None)
      )
    )
  }

  test("three") {
    ==(
      slidingList("a", "b", "c"),
      List(
        ("a", Some("b"), Some("c")),
        ("b", Some("c"), None),
        ("c", None, None)
      )
    )
  }

  test("four") {
    ==(
      slidingList("a", "b", "c", "d"),
      List(
        ("a", Some("b"), Some("c")),
        ("b", Some("c"), Some("d")),
        ("c", Some("d"), None),
        ("d", None, None)
      )
    )
  }

  test("five") {
    ==(
      slidingList("a", "b", "c", "d", "e"),
      List(
        ("a", Some("b"), Some("c")),
        ("b", Some("c"), Some("d")),
        ("c", Some("d"), Some("e")),
        ("d", Some("e"), None),
        ("e", None, None)
      )
    )
  }
}
