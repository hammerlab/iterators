package org.hammerlab.iterator.scan

import cats.implicits.{ catsKernelStdGroupForInt, catsKernelStdMonoidForString }
import org.hammerlab.iterator.scan.ScanValuesIterator._
import org.hammerlab.test.Suite
import org.hammerlab.types.Monoid._

class ScanValuesIteratorTest
  extends Suite {

  test("left ints") {
    Array(
      'a' → 1,
      'b' → 2,
      'c' → 3,
      'd' → 4
    )
    .scanLeftValues
    .toList should be(
      List(
        'a' →  1,
        'b' →  3,
        'c' →  6,
        'd' → 10
      )
    )
  }

  test("left strings") {
    List(
      'a' → "z",
      'b' → "y",
      'c' → "x",
      'd' → "w"
    )
    .scanLeftValues
    .toList should be(
      List(
        'a' → "z",
        'b' → "zy",
        'c' → "zyx",
        'd' → "zyxw"
      )
    )
  }

  test("left case classes") {
    Iterator(
      'a' → Foo(111, "aaa"),
      'b' → Foo(222, "bbb"),
      'c' → Foo(333, "ccc")
    )
    .scanLeftValues
    .toList should be(
      List(
        'a' → Foo(111, "aaa"),
        'b' → Foo(333, "aaabbb"),
        'c' → Foo(666, "aaabbbccc")
      )
    )
  }

  test("right ints") {
    Array(
      'a' → 1,
      'b' → 2,
      'c' → 3,
      'd' → 4
    )
    .scanRightValues
    .toList should be(
      List(
        'a' → 10,
        'b' →  9,
        'c' →  7,
        'd' →  4
      )
    )
  }

  test("right strings") {
    List(
      'a' → "z",
      'b' → "y",
      'c' → "x",
      'd' → "w"
    )
    .scanRightValues
    .toList should be(
      List(
        'a' → "zyxw",
        'b' → "yxw",
        'c' → "xw",
        'd' → "w"
      )
    )
  }

  test("right case classes") {
    Iterator(
      'a' → Foo(111, "aaa"),
      'b' → Foo(222, "bbb"),
      'c' → Foo(333, "ccc")
    )
    .scanRightValues
    .toList should be(
      List(
        'a' → Foo(666, "aaabbbccc"),
        'b' → Foo(555, "bbbccc"),
        'c' → Foo(333, "ccc")
      )
    )
  }

}
