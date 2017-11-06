package org.hammerlab.iterator.sample

import hammerlab.iterator.sample._
import org.hammerlab.test.Suite

import scala.util.Random

class SampleTest
  extends Suite {

  before {
    Random.setSeed(123)
  }

  def check(numToSample: Int)(expected: Int*)(implicit numInputElems: Int): Unit = {
    (1 to numInputElems).iterator.sample(numToSample) should be(expected)
    Iterator(1,2,3)
  }

  {
    implicit val numInputElems = 0
    test("zero 0") { check(0)() }
    test("zero 10") { check(10)() }
  }

  {
    implicit val numInputElems = 1
    test("one 0")  { check( 0)( ) }
    test("one 1")  { check( 1)(1) }
    test("one 2")  { check( 2)(1) }
    test("one 10") { check(10)(1) }
  }

  {
    implicit val numInputElems = 2
    test("two 0")  { check( 0)() }
    test("two 1")  { check( 1)(2) }
    test("two 2")  { check( 2)(1, 2) }
    test("two 3")  { check( 3)(1, 2) }
    test("two 10") { check(10)(1, 2) }
  }

  {
    implicit val numInputElems = 3
    test("three 0")  { check( 0)() }
    test("three 1")  { check( 1)(3) }
    test("three 2")  { check( 2)(1, 3) }
    test("three 3")  { check( 3)(1, 2, 3) }
    test("three 4")  { check( 4)(1, 2, 3) }
    test("three 10") { check(10)(1, 2, 3) }
  }

  {
    implicit val numInputElems = 4
    test("four 0")  { check( 0)() }
    test("four 1")  { check( 1)(3) }
    test("four 2")  { check( 2)(1, 3) }
    test("four 3")  { check( 3)(1, 2, 4) }
    test("four 4")  { check( 4)(1, 2, 3, 4) }
    test("four 5")  { check( 5)(1, 2, 3, 4) }
    test("four 10") { check(10)(1, 2, 3, 4) }
  }

  {
    implicit val numInputElems = 5
    test("five 0")  { check( 0)() }
    test("five 1")  { check( 1)(3) }
    test("five 2")  { check( 2)(1, 3) }
    test("five 3")  { check( 3)(2, 4, 5) }
    test("five 4")  { check( 4)(1, 2, 4, 5) }
    test("five 5")  { check( 5)(1, 2, 3, 4, 5) }
    test("five 10") { check(10)(1, 2, 3, 4, 5) }
  }

  {
    implicit val numInputElems = 6
    test("six 0")  { check( 0)() }
    test("six 1")  { check( 1)(6) }
    test("six 2")  { check( 2)(1, 3) }
    test("six 3")  { check( 3)(4, 5, 6) }
    test("six 4")  { check( 4)(2, 4, 5, 6) }
    test("six 5")  { check( 5)(1, 2, 4, 5, 6) }
    test("six 6")  { check( 6)(1, 2, 3, 4, 5, 6) }
    test("six 10") { check(10)(1, 2, 3, 4, 5, 6) }
  }

  {
    implicit val numInputElems = 7
    test("seven 0")  { check( 0)() }
    test("seven 1")  { check( 1)(6) }
    test("seven 2")  { check( 2)(1, 3) }
    test("seven 3")  { check( 3)(4, 5, 6) }
    test("seven 4")  { check( 4)(2, 4, 6, 7) }
    test("seven 5")  { check( 5)(1, 2, 4, 5, 7) }
    test("seven 6")  { check( 6)(1, 2, 4, 5, 6, 7) }
    test("seven 7")  { check( 7)(1, 2, 3, 4, 5, 6, 7) }
    test("seven 10") { check(10)(1, 2, 3, 4, 5, 6, 7) }
  }
}
