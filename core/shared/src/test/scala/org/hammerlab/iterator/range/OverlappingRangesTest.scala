package org.hammerlab.iterator.range

import hammerlab.iterator.range._
import org.hammerlab.Suite

class OverlappingRangesTest
  extends Suite {

  def Ranges(ranges: (Range[Int], Int)*): Seq[(Range[Int], Int)] = ranges

  implicit def intToRange(n: Int): Range[Int] = Range(n, None)
  implicit def pairToIntRange(t: (Int, Int)): Range[Int] = Range(t._1, t._2)
  implicit def indexedPairToRange(t: ((Int, Int), Int)): (Range[Int], Int) = (t._1, t._2)

  implicit def makeJoinedRangesElem(t: (Int, Seq[Range[Int]])): (Range[Int], Seq[Range[Int]]) =
    (t._1: Range[Int]) → t._2

  implicit def makeHalfOpenJoinedRangesElem(t: ((Int, Int), Seq[Range[Int]])): (Range[Int], Seq[Range[Int]]) =
    (t._1: Range[Int]) → t._2

  def check(left: Range[Int]*)(right: Range[Int]*)(expected: Seq[(Range[Int], Int)]*): Unit = {
    left
      .iterator
      .joinOverlaps(
        right
          .iterator
          .buffered
      )
      .toList should be(
      left.zip(expected)
    )
  }

  test("trivial case") {
    check(
      1 → 3
    )(
      1 → 3
    )(
      Seq(Range(1, 3) → 0)
    )
  }

  test("stable right-side ordering") {
    check(
       2 → 4,
       5 → 7,
       8
    )(
       0 →  2,
       1 →  3,
       2 →  5,
       4 →  5,
       4 →  6,
       4 →  9,
       4 →  8,
       6 →  7,
       7 → 11,
      13 → 14,
      13
    )(
      Seq(Range(1, 3) → 1, Range(2,  5) → 2),
      Seq(Range(4, 6) → 4, Range(4,  9) → 5, Range( 4,  8) → 6, Range( 6, 7) →  7),
      Seq(Range(4, 9) → 5, Range(7, 11) → 8, Range(13, 14) → 9, Range(13)    → 10)
    )
  }

  test("all rights before") {
    check(
      5 → 10,
      7 →  9
    )(
      0 → 2,
      0 → 3,
      1 → 5
    )(
      Nil,
      Nil
    )
  }

  test("all rights after") {
    check(
      5 → 10,
      7 →  9
    )(
      10 → 12,
      10 → 13,
      11 → 15
    )(
      Nil,
      Nil
    )
  }

  test("fully skipped rights") {
    check(
       2 →  4,
       3 →  5,
      10 → 15
    )(
       1 →  2,
       3 →  4,
       5 → 10,
       6 →  9,
      11 → 12,
      15 → 20
    )(
      Seq(Range( 3,  4) → 1),
      Seq(Range( 3,  4) → 1),
      Seq(Range(11, 12) → 4)
    )
  }
}
