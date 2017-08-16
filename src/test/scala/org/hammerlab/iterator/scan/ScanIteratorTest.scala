package org.hammerlab.iterator.scan

import cats.implicits.{ catsKernelStdGroupForInt, catsKernelStdMonoidForString }
import org.hammerlab.iterator.scan.ScanIterator._
import org.hammerlab.test.Suite
import org.hammerlab.types.Monoid._

class ScanIteratorTest
  extends Suite {
  test("scanL ints") {
    (1 to 10)
      .scanL  // convert from Iterable
      .toList should be(
      List(
         1,
         3,
         6,
        10,
        15,
        21,
        28,
        36,
        45,
        55
      )
    )
  }

  test("scanL strings") {
    "abcde"
      .split("")
      .scanL  // convert from Array
      .toList should be(
      List(
        "a",
        "ab",
        "abc",
        "abcd",
        "abcde"
      )
    )
  }

  test("scanL case class") {
    Iterator(
      Foo(111, "aaa"),
      Foo(222, "bbb"),
      Foo(333, "ccc")
    )
    .scanL  // convert from Iterator
    .toList should be(
      List(
        Foo(111, "aaa"),
        Foo(333, "aaabbb"),
        Foo(666, "aaabbbccc")
      )
    )
  }

  test("scanR ints") {
    (1 to 10)
      .scanR  // convert from Iterable
      .toList should be(
        List(
          55,
          54,
          52,
          49,
          45,
          40,
          34,
          27,
          19,
          10
        )
      )
  }

  test("scanR strings") {
    "abcde"
      .split("")
      .scanR  // convert from Array
      .toList should be(
      List(
        "abcde",
         "bcde",
          "cde",
           "de",
            "e"
      )
    )
  }

  test("scanR case class") {
    Iterator(
      Foo(111, "aaa"),
      Foo(222, "bbb"),
      Foo(333, "ccc")
    )
    .scanR  // convert from Iterator
    .toList should be(
      List(
        Foo(666, "aaabbbccc"),
        Foo(555, "bbbccc"),
        Foo(333, "ccc")
      )
    )
  }

}

case class Foo(n: Int, s: String)
