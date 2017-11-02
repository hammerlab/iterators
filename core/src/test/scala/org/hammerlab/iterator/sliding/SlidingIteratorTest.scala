package org.hammerlab.iterator.sliding

import org.hammerlab.iterator.sliding.SlidingIterator._
import org.hammerlab.test.Suite

class SlidingIteratorTest
  extends Suite {

  def check(elems: String*)(n: Int)(expected: String): Unit = {
    val actual =
      Iterator(elems: _*)
        .slide(n)
        .map(_.mkString(""))
        .mkString(" ")

    actual should be(expected)
  }

  test("empty") {
    check()(0)("")
    check()(1)("")
    check()(2)("")
    check()(3)("")
    check()(4)("")
  }

  test("one") {
    check("a")(0)("")
    check("a")(1)("a")
    check("a")(2)("a")
    check("a")(3)("a")
    check("a")(4)("a")
  }

  test("two") {
    check("a", "b")(0)("")
    check("a", "b")(1)("a b")
    check("a", "b")(2)("ab b")
    check("a", "b")(3)("ab b")
    check("a", "b")(4)("ab b")
  }

  test("three") {
    check("a", "b", "c")(0)("")
    check("a", "b", "c")(1)("a b c")
    check("a", "b", "c")(2)("ab bc c")
    check("a", "b", "c")(3)("abc bc c")
    check("a", "b", "c")(4)("abc bc c")
  }

  test("four") {
    check("a", "b", "c", "d")(0)("")
    check("a", "b", "c", "d")(1)("a b c d")
    check("a", "b", "c", "d")(2)("ab bc cd d")
    check("a", "b", "c", "d")(3)("abc bcd cd d")
    check("a", "b", "c", "d")(4)("abcd bcd cd d")
  }
}
