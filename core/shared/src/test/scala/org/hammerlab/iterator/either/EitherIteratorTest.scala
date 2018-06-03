package org.hammerlab.iterator.either

import hammerlab.iterator._
import org.hammerlab.iterator.either.EitherIteratorTest._
import org.hammerlab.Suite

class FindLeftTest
  extends Suite {

  implicit def intOpt(n: Int): Option[Int] = Some(n)

  def check(elems: Either[Int, String]*)(expected: Option[Int] = None): Unit =
    ==(
      eithers(elems).findLeft,
      expected
    )

  test("findleft") {
    check(4, 5, "abc", 6)(4)
    check("abc", 6, "def", 7)(6)
    check("abc", "def", 8)(8)
    check()()
    check("abc")()
    check("abc", "def")()
    check(4)(4)
  }
}

class GroupByLeftTest
  extends Suite {

  def check(elems: Either[Int, String]*)(expected: (Int, String)*): Unit =
    ==(
      eithers(elems)
        .groupByLeft
        .map {
          case (num, strings) ⇒
            num →
              strings.mkString("")
        }
        .toList,
      expected
    )

  test("simple") {
    check(
      1, "a", "b", "c",
      2,
      3,
      4, "d",
      5
    )(
      1 → "abc",
      2 → "",
      3 → "",
      4 → "d",
      5 → ""
    )
  }

  test("rights first and last") {
    check(
      "a", "b", "c",
      1, "d", "e",
      2,
      3, "f", "g"
    )(
      1 → "de",
      2 → "",
      3 → "fg"
    )
  }

  test("rights not consumed") {
    ==(
      eithers(
        Seq(
          1, "a", "b", "c",
          2,
          3, "d",
          4, "e", "f",
          5
        )
      )
      .groupByLeft
      .map {
        case (num, strings) ⇒
          num →
            strings
              .buffered
              .headOption
              .getOrElse("???")
      }
      .toList,
      Seq(
        1 → "a",
        2 → "???",
        3 → "d",
        4 → "e",
        5 → "???"
      )
    )
  }
}

class RoundUpRightTest
  extends Suite {

  def check(elems: Either[Int, String]*)(expected: (Seq[Int], String)*): Unit =
    ==(
      eithers(elems)
        .roundUpRight
        .toList,
      expected
    )

  test("mixed") {
    check(
      1, 2, 3, "abc",
      "def",
      4, "ghi",
      5
    )(
      Seq(1, 2, 3) → "abc",
      Nil → "def",
      Seq(4) → "ghi"
    )
  }

  test("rights first") {
    check(
      "abc",
      "def",
      1, "ghi",
      "jkl",
      2, 3, "mno",
      "pqr"
    )(
      Nil → "abc",
      Nil → "def",
      Seq(1) → "ghi",
      Nil → "jkl",
      Seq(2, 3) → "mno",
      Nil → "pqr"
    )
  }

}

object EitherIteratorTest {
  implicit def leftInt(n: Int): Left[Int, String] = Left(n)
  implicit def rightString(s: String): Right[Int, String] = Right(s)

  def eithers(elems: Seq[Either[Int, String]]): BufferedIterator[Either[Int, String]] = Iterator(elems: _*).buffered
}
