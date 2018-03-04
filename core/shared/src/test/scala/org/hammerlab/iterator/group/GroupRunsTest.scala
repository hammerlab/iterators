package org.hammerlab.iterator.group

import hammerlab.iterator.group._
import org.hammerlab.Suite

class GroupRunsPredicateTest extends Suite {

  def check(ints: Int*)(strs: String*): Unit =
    ints
      .groupRunsFn(_ % 2 == _ % 2)
      .map(_.mkString(","))
      .toList should be(strs)

  test("end with a run") {
    check(
      1, 2, 5, 4, 6, 8
    )(
      "1", "2", "5", "4,6,8"
    )
  }

  test("start and end with a run") {
    check(
      2, 4, 6, 8, 1, 10, 3, 5, 7
    )(
      "2,4,6,8", "1", "10", "3,5,7"
    )
  }

  test("one run") {
    check(
      2, 4, 6, 8
    )(
      "2,4,6,8"
    )
  }

  test("empty") {
    check()()
  }

  test("no runs") {
    check(
      1, 2, 3, 4
    )(
      "1", "2", "3", "4"
    )
  }

  test("singleton") {
    check(2)("2")
  }
}

class GroupRunsTest
  extends Suite {

  def check(tuples: (Int, Int)*)(strs: String*): Unit = {
    implicit val intOrder = Ordering.by[(Int, Int), Int](_._1)
    tuples
      .groupRuns
      .map(_.map(t ⇒ s"${t._1},${t._2}").mkString(" "))
      .toList should be(strs.toList)
  }

  test("empty") {
    check()()
  }

  test("one") {
    check(1 → 2)("1,2")
  }

  test("two same") {
    check(1 → 2, 1 → 3)("1,2 1,3")
  }

  test("two different") {
    check(1 → 2, 4 → 1)("1,2", "4,1")
  }

  test("three same") {
    check(1 → 2, 1 → 3, 1 → 0)("1,2 1,3 1,0")
  }

  test("two same one different") {
    check(1 → 2, 1 → 3, 4 → 1)("1,2 1,3", "4,1")
  }

  test("three same skip one") {
    check(1 → 2, 1 → 3, 4 → 1, 1 → 0)("1,2 1,3", "4,1", "1,0")
  }

  test("two same two same") {
    check(1 → 2, 1 → 3, 4 → 1, 4 → 5)("1,2 1,3", "4,1 4,5")
  }

  test("one then three") {
    check(1 → 2, 4 → 6, 4 → 1, 4 → 2)("1,2", "4,6 4,1 4,2")
  }
}
