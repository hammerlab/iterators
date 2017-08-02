package org.hammerlab.stats

import Double.NaN
import org.hammerlab.test.Suite
import org.scalactic.Equality
import shapeless.{ Generic, HNil }
import spire.math.Numeric

class StatsTest
  extends Suite {

  implicit val de =
    new Equality[Double] {
      override def areEqual(a: Double, b: Any): Boolean =
        b match {
          case d: Double ⇒
            if (a.isNaN && d.isNaN) true
            else a == d
          case _ ⇒
            false
        }
    }

  def check[K : Numeric : Ordering](input: Seq[K],
                                    expected: Stats[K, Int]): Unit =
    Stats(input) should ===(expected)

  def check[K : Numeric : Ordering](input: Seq[K],
                                    numToSample: Int,
                                    expected: Stats[K, Int]): Unit =
    Stats(
      input,
      numToSample
    ) should be(
      expected
    )

  def check[K : Numeric : Ordering](input: Seq[K],
                                    numToSample: Int,
                                    onlySampleSorted: Boolean,
                                    expected: Stats[K, Int]): Unit =
    Stats(
      input,
      numToSample,
      onlySampleSorted
    ) should be(
      expected
    )

  test("empty") {
    val stats = Stats[Int](Nil)
    val hl = Generic[Stats[Int, Int]].to(stats)
    hl should be(0 :: NaN :: NaN :: NaN :: NaN :: None :: None :: List() :: HNil)
//    check[Int](
//      Nil,
//      Stats.empty[Int, Int]
//    )
  }

//  test("0 to 0") {
//    check(
//      0 to 0,
//      "num:	1,	mean:	0,	stddev:	0,	mad:	0",
//      "elems:	0"
//    )
//  }
//
//  test("0 to 1") {
//    check(
//      0 to 1,
//      "num:	2,	mean:	0.5,	stddev:	0.5,	mad:	0.5",
//      "elems:	0, 1"
//    )
//  }
//
//  test("1 to 0") {
//    check(
//      1 to 0 by -1,
//      "num:	2,	mean:	0.5,	stddev:	0.5,	mad:	0.5",
//      "elems:	1, 0",
//      "sorted:	0, 1"
//    )
//  }
//
//  test("0 to 2") {
//    check(
//      0 to 2,
//      "num:	3,	mean:	1,	stddev:	0.8,	mad:	1",
//      "elems:	0, 1, 2",
//      "50:	1"
//    )
//  }

}
