package org.hammerlab.iterator.group

import hammerlab.iterator.group._
import org.hammerlab.test.Suite

class UnorderedSubsetsTest
  extends Suite {

  def binomial(n: Int, k: Int): Long =
    if (k > n - k)
      binomial(n, n - k)
    else {
      var b = 1
      var i = 1
      var m = n
      while (i <= k) {
        b = b * m / i
        i += 1
        m -= 1
      }
      b
    }

  def check[T](elems: T*)(expecteds: Seq[Seq[(T, Int)]]*): Unit =
    for {
      (expected, n) ← expecteds.zipWithIndex
    } {
      withClue(s"n: $n: ") {
        val actual =
          elems
            .unorderedSubsetsWithReplacement(n)
            .toList

        actual.size should be(
          binomial(
            elems.size + n - 1,
            n
          )
        )
        actual should be(expected)
      }
    }

  test("empty") {
    check()(Seq(Nil), Seq(Nil), Seq(Nil), Seq(Nil))
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
