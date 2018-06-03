package org.hammerlab.iterator.group

import cats.Eq
import hammerlab.iterator.group._
import org.hammerlab.Suite

class RunLengthTest
  extends Suite {

  def check[T: Cmp: org.hammerlab.test.Cmp](elems: T*)(expected: (T, Int)*): Unit =
    ==(
      elems
        .runLengthEncode
        .toList,
      expected
    )

  test("empty") {
    check[Int]()()
  }

  test("single") {
    check("a")("a" → 1)
  }

  test("one run") {
    check("a", "a", "a")("a" → 3)
  }

  test("two singletons") {
    check("a", "b")("a" → 1, "b" → 1)
  }

  test("run singleton") {
    check("a", "a", "a", "b")("a" → 3, "b" → 1)
  }

  test("singleton run") {
    check("a", "b", "b", "b")("a" → 1, "b" → 3)
  }

  test("single run single") {
    check("z", "y", "y", "x")("z" → 1, "y" → 2, "x" → 1)
  }

  test("two runs") {
    check("a", "a", "a", "b", "b", "b", "b")("a" → 3, "b" → 4)
  }

  val parityInts =
    Seq(
      1, 5, 3, 7,
      2,
      5, 5,
      4, 6, 8,
      1,
      2
    )

  val foldedLeft =
    Seq(
      1 → 4,
      2 → 1,
      5 → 2,
      4 → 3,
      1 → 1,
      2 → 1
    )

  val foldedRight =
    Seq(
      7 → 4,
      2 → 1,
      5 → 2,
      8 → 3,
      1 → 1,
      2 → 1
    )

  {

    test("evens and odds") {
      {
        /** Name overrides [[Suite.intOrder]] which would be used by default in this [[Suite]] instance */
        implicit val intOrder =
          new Eq[Int] {
            override def eqv(x: Int, y: Int): Boolean = x % 2 == y % 2
          }

        ==(parityInts.runLengthEncode.toSeq, foldedLeft)
      }

      {
        /** Name overrides [[Suite.intOrder]] which would be used by default in this [[Suite]] instance */
        implicit val intOrder = Ordering.by[Int, Int](_ % 2)

        ==(parityInts.runLengthEncode.toSeq, foldedLeft)
      }
    }
  }

  test("partial function") {
    ==(
      parityInts
        .runLengthPartial {
          case (l, r) if l % 2 == r % 2 ⇒ r
        }
        .toList,
      foldedRight
    )
  }

  test("total function") {
    ==(
      parityInts
        .runLengthFunction {
          case (l, r) if l % 2 == r % 2 ⇒ Some(r)
          case _ ⇒ None
        }
        .toList,
        foldedRight
      )
  }

  {
    test("re-encode") {
      ==(
        Iterator('a' → 2, 'a' → 1, 'b' → 3, 'a' → 4, 'c' → 1, 'c' → 1, 'c' → 2, 'a' → 1)
          .runLengthReencode
          .toList,
        List(
          'a' → 3,
          'b' → 3,
          'a' → 4,
          'c' → 4,
          'a' → 1
        )
      )
    }
  }
}
