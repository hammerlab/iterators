package org.hammerlab.iterator.sliding

import hammerlab.iterator.sliding._
import org.hammerlab.Suite

class Sliding3Test extends Suite {

  def slidingList[T](elems: T*): List[(T, T, T)] =
    Iterator(elems: _*)
      .sliding3
      .toList

  test("empty") {
    ==(slidingList(), Nil)
  }

  test("one") {
    ==(slidingList("a"), Nil)
  }

  test("two") {
    ==(slidingList("a", "b"), Nil)
  }

  test("three") {
    ==(
      slidingList("a", "b", "c"),
      List(
        ("a", "b", "c")
      )
    )
  }

  test("four") {
    ==(
      slidingList("a", "b", "c", "d"),
      List(
        ("a", "b", "c"),
        ("b", "c", "d")
      )
    )
  }

  test("five") {
    ==(
      slidingList("a", "b", "c", "d", "e"),
      List(
        ("a", "b", "c"),
        ("b", "c", "d"),
        ("c", "d", "e")
      )
    )
  }
}
