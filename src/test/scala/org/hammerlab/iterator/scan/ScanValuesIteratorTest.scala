package org.hammerlab.iterator.scan

import cats.implicits.{ catsKernelStdGroupForInt, catsKernelStdMonoidForString }
import org.hammerlab.iterator.scan.ScanValuesIterator._
import org.hammerlab.test.Suite
import org.hammerlab.types.Monoid._

trait ScanValuesIteratorTest
  extends Suite {

  type K = Char

  def ints: Seq[(K, Int)]
  def strings: Seq[(K, String)]
  def foos: Seq[(K, Foo)]

  def check[T](it: Iterator[(K, T)], expected: Seq[(K, T)]): Unit = {
    it.toList should be(expected)
  }

  def intsInput =
    Array(
      'a' → 1,
      'b' → 2,
      'c' → 3,
      'd' → 4
    )

  def stringsInput =
    List(
      'a' → "z",
      'b' → "y",
      'c' → "x",
      'd' → "w"
    )

  def foosInput =
    Iterator(
      'a' → Foo(111, "aaa"),
      'b' → Foo(222, "bbb"),
      'c' → Foo(333, "ccc")
    )
}

abstract class ScanLeftValuesIteratorTest(includeCurrentValues: Boolean)
  extends ScanValuesIteratorTest {

  test("left ints") {
    check(
      intsInput.scanLeftValues(includeCurrentValues),
      ints
    )
  }

  test("left strings") {
    check(
      stringsInput.scanLeftValues(includeCurrentValues),
      strings
    )
  }

  test("left case classes") {
    check(
      foosInput.scanLeftValues(includeCurrentValues),
      foos
    )
  }
}

class ScanLeftValuesInclusiveTest
  extends ScanLeftValuesIteratorTest(true) {
  override def ints: Seq[(K, Int)] =
    Seq(
      'a' →  1,
      'b' →  3,
      'c' →  6,
      'd' → 10
    )

  override def strings: Seq[(K, String)] =
    Seq(
      'a' → "z",
      'b' → "zy",
      'c' → "zyx",
      'd' → "zyxw"
    )

  override def foos: Seq[(K, Foo)] =
    Seq(
      'a' → Foo(111, "aaa"),
      'b' → Foo(333, "aaabbb"),
      'c' → Foo(666, "aaabbbccc")
    )
}

class ScanLeftValuesExclusiveTest
  extends ScanLeftValuesIteratorTest(false) {
  override def ints: Seq[(K, Int)] =
    Seq(
      'a' → 0,
      'b' → 1,
      'c' → 3,
      'd' → 6
    )

  override def strings: Seq[(K, String)] =
    Seq(
      'a' → "",
      'b' → "z",
      'c' → "zy",
      'd' → "zyx"
    )

  override def foos: Seq[(K, Foo)] =
    Seq(
      'a' → Foo(  0,       ""),
      'b' → Foo(111,    "aaa"),
      'c' → Foo(333, "aaabbb")
    )
}


abstract class ScanRightValuesIteratorTest(includeCurrentValues: Boolean)
  extends ScanValuesIteratorTest {

  test("right ints") {
    check(
      intsInput.scanRightValues(includeCurrentValues),
      ints
    )
  }

  test("right strings") {
    check(
      stringsInput.scanRightValues(includeCurrentValues),
      strings
    )
  }

  test("right case classes") {
    check(
      foosInput.scanRightValues(includeCurrentValues),
      foos
    )
  }
}

class ScanRightValuesInclusiveTest
  extends ScanRightValuesIteratorTest(true) {

  override def ints: Seq[(K, Int)] =
    Seq(
      'a' → 10,
      'b' →  9,
      'c' →  7,
      'd' →  4
    )

  override def strings: Seq[(K, String)] =
    Seq(
      'a' → "zyxw",
      'b' → "yxw",
      'c' → "xw",
      'd' → "w"
    )

  override def foos: Seq[(K, Foo)] =
    Seq(
      'a' → Foo(666, "aaabbbccc"),
      'b' → Foo(555, "bbbccc"),
      'c' → Foo(333, "ccc")
    )
}

class ScanRightValuesExclusiveTest
  extends ScanRightValuesIteratorTest(false) {

  override def ints: Seq[(K, Int)] =
    Seq(
      'a' →  9,
      'b' →  7,
      'c' →  4,
      'd' →  0
    )

  override def strings: Seq[(K, String)] =
    Seq(
      'a' → "yxw",
      'b' → "xw",
      'c' → "w",
      'd' → ""
    )

  override def foos: Seq[(K, Foo)] =
    Seq(
      'a' → Foo(555, "bbbccc"),
      'b' → Foo(333,    "ccc"),
      'c' → Foo(  0,       "")
    )
}
