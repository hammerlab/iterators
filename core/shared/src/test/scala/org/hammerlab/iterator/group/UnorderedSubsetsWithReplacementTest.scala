package org.hammerlab.iterator.group

import hammerlab.iterator.group._
import hammerlab.math.binomial
import org.hammerlab.Suite

import scala.math.max

class UnorderedSubsetsWithReplacementTest
  extends Suite {

  def check[T](elems: T*)(expecteds: Seq[Seq[(T, Int)]]*): Unit =
    for {
      (expected, k) ← expecteds.zipWithIndex
    } {
      withClue(s"k: $k: ") {
        val actual =
          elems
            .unorderedSubsetsWithReplacement(k)
            .toList

        actual.size should be(
          binomial(
            max(0, elems.size + k - 1),
            k
          )
        )
        actual should be(expected)
      }
    }

  test("empty") {
    check()(Seq(Nil), Nil, Nil, Nil)
  }

  test("singleton") {
    check('a)(
      Seq(Nil),
      Seq(Seq('a → 1)),
      Seq(Seq('a → 2)),
      Seq(Seq('a → 3))
    )
  }

  test("2 elems") {
    check('a, 'b)(
      Seq(Nil),
      Seq(Seq('a → 1), Seq(        'b → 1)),
      Seq(Seq('a → 2), Seq('a → 1, 'b → 1), Seq(        'b → 2)),
      Seq(Seq('a → 3), Seq('a → 2, 'b → 1), Seq('a → 1, 'b → 2), Seq(        'b → 3)),
      Seq(Seq('a → 4), Seq('a → 3, 'b → 1), Seq('a → 2, 'b → 2), Seq('a → 1, 'b → 3), Seq('b → 4))
    )
  }

  test("3 elems") {
    check('a, 'b, 'c)(
      Seq(Nil),
      Seq(Seq('a → 1), Seq('b → 1), Seq('c → 1)),
      Seq(
        Seq('a → 2),
        Seq('a → 1, 'b → 1), Seq('a → 1, 'c → 1),
        Seq('b → 2), Seq('b → 1, 'c → 1), Seq('c → 2)),
      Seq(
        Seq('a → 3),
        Seq('a → 2, 'b → 1), Seq('a → 2, 'c → 1),
        Seq('a → 1, 'b → 2), Seq('a → 1, 'b → 1, 'c → 1), Seq('a → 1, 'c → 2),
        Seq(        'b → 3), Seq('b → 2, 'c → 1), Seq('b → 1, 'c → 2),
        Seq('c → 3)
      ),
      Seq(
        Seq('a → 4),
        Seq('a → 3, 'b → 1), Seq('a → 3, 'c → 1),
        Seq('a → 2, 'b → 2), Seq('a → 2, 'b → 1, 'c → 1), Seq('a → 2, 'c → 2),
        Seq('a → 1, 'b → 3), Seq('a → 1, 'b → 2, 'c → 1), Seq('a → 1, 'b → 1, 'c → 2), Seq('a → 1, 'c → 3),
        Seq('b → 4), Seq('b → 3, 'c → 1), Seq('b → 2, 'c → 2), Seq('b → 1, 'c → 3),
        Seq('c → 4)
      )
    )
  }

  test("larger sizes") {
    for {
      n ← 1 to 10
      k ← 0 to 10
    } {
      withClue(s"($n,$k): ") {
        val list = (1 to n).unorderedSubsetsWithReplacement(k).toList
        val expected =
          binomial(
            n + k - 1,
            k
          )
        list.size should be(expected)
        list.toSet.size should be(expected)
      }
    }
  }
}
