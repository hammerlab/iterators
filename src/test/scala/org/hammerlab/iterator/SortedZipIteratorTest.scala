package org.hammerlab.iterator

import org.hammerlab.iterator.SortedZipIterator._
import org.hammerlab.test.Suite

import scala.collection.immutable.StringOps

abstract class SortedZipIteratorTest
  extends Suite {

  type Result

  def check(left: Int*)(right: Int*)(expected: Result*): Unit
}

trait SortedZipIntsTest {

  self: SortedZipIteratorTest ⇒

  def L(t: Int): Result
  def R(u: Int): Result

  test("135 246") {
    check(
      1, 3, 5
    )(
      2, 4, 6
    )(
      L(1),
      R(2),
      L(3),
      R(4),
      L(5),
      R(6)
    )
  }

  test("123 456") {
    check(
      1, 2, 3
    )(
      4, 5, 6
    )(
      L(1),
      L(2),
      L(3),
      R(4),
      R(5),
      R(6)
    )
  }

  test("123 123") {
    check(
      1, 2, 3
    )(
      1, 2, 3
    )(
      L(1),
      R(1),
      L(2),
      R(2),
      L(3),
      R(3)
    )
  }

  test("both empty") {
    check()()()
  }

  test("one empty one 1") {
    check()(1)(R(1))
  }

  test("one empty one 3") {
    check(

    )(
      1, 10, 100
    )(
      R(1),
      R(10),
      R(100)
    )
  }

  test("L 1 R empty") {
    check(1)()(L(1))
  }

  test("L 3 R empty") {
    check(
      1, 10, 100
    )(

    )(
      L(1),
      L(10),
      L(100)
    )
  }
}

abstract class SortedEitherZip
  extends SortedZipIteratorTest {

  type L
  type R
  type Result = Either[L, R]

  override def check(left: Int*)(right: Int*)(expected: Result*): Unit = {
    left
      .iterator
      .sortedEitherZip(right.iterator)
      .toList should be(
      expected
    )
  }

  def L(l: L): Result = Left(l)
  def R(r: R): Result = Right(r)
}

class SortedEitherInts
  extends SortedEitherZip
    with SortedZipIntsTest {

  type L = Int
  type R = Int
}

trait IntStringEitherTest
  extends SortedEitherZip {

  /**
   * Workaround [[strlen]] making [[augmentString]] implicit (for accessing
   * [[scala.collection.immutable.StringLike.*]]) ambiguous.
   */
  implicit class StringMult(val s: String) {
    def ｘ(n: Int): String = (s: StringOps) * n
  }

  case class WrappedInt(n: Int)
  implicit val wrapInt: Int ⇒ WrappedInt = WrappedInt
  implicit val unwrapWrappedInt: WrappedInt ⇒ Int = _.n

  val wrappedInts =
    Seq[WrappedInt](
       1,
       2,
       4,
       7,
      10,
      15
    )

  val strings =
    Seq(
      "",
      "a",
      "a",
      "bb",
      "c" ｘ 3,
      "e" ｘ 5,
      "f" ｘ 6,
      "k" ｘ 11,
      "n" ｘ 14
    )

  implicit def strlen(s: String): Int = s.length
}

class SortedEitherIntStringTest
  extends IntStringEitherTest {

  override type L = WrappedInt
  override type R = String

  test("different types") {
    wrappedInts
      .iterator
      .sortedEitherZip[String, Int](
        strings.iterator
      )
      .toList should be(
        Seq[Either[WrappedInt, String]](
          R(""),
          L(1),
          R("a"),
          R("a"),
          L(2),
          R("bb"),
          R("ccc"),
          L(4),
          R("eeeee"),
          R("ffffff"),
          L(7),
          L(10),
          R("kkkkkkkkkkk"),
          R("nnnnnnnnnnnnnn"),
          L(15)
        )
      )
  }
}

class SortedEitherStringIntTest
  extends IntStringEitherTest  {

  override type L = String
  override type R = WrappedInt

  test("different types") {
    strings
      .iterator
      .sortedEitherZip[WrappedInt, Int](
        wrappedInts.iterator
      )
      .toList should be(
        Seq[Either[String, WrappedInt]](
          L(""),
          L("a"),
          L("a"),
          R(1),
          L("bb"),
          R(2),
          L("ccc"),
          R(4),
          L("eeeee"),
          L("ffffff"),
          R(7),
          R(10),
          L("kkkkkkkkkkk"),
          L("nnnnnnnnnnnnnn"),
          R(15)
        )
      )
  }
}
