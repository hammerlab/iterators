package org.hammerlab.iterator.group

import hammerlab.iterator.group._
import org.hammerlab.Suite

class UnorderedSubsetsTest
  extends Suite {

  def check[T](elems: T*)(expecteds: Seq[Seq[T]]*): Unit =
    for {
      (expected, k) ← expecteds.zipWithIndex
    } {
      withClue(s"k: $k: ") {
        val actual =
          elems
            .unorderedSubsets(k)
            .toList

        actual should be(expected)
        actual.size should be(
          Binomial(
            elems.size,
            k
          )
        )
      }
    }

  test("empty") {
    check()(Seq(Nil), Nil, Nil, Nil)
  }

  test("1 element") {
    check('a)(
      Seq(Nil),
      Seq(Seq('a)),
      Nil
    )
  }

  test("2 elements") {
    check('a, 'b)(
      Seq(Nil),
      Seq(Seq('a), Seq('b)),
      Seq(Seq('a, 'b)),
      Nil
    )
  }

  test("3 elements") {
    check('a, 'b, 'c)(
      Seq(Nil),
      Seq(Seq('a), Seq('b), Seq('c)),
      Seq(Seq('a, 'b), Seq('a, 'c), Seq('b, 'c)),
      Seq(Seq('a, 'b, 'c)),
      Nil
    )
  }

  test("4 elements") {
    check('a, 'b, 'c, 'd)(
      Seq(Nil),
      Seq(Seq('a), Seq('b), Seq('c), Seq('d)),
      Seq(Seq('a, 'b), Seq('a, 'c), Seq('a, 'd), Seq('b, 'c), Seq('b, 'd), Seq('c, 'd)),
      Seq(Seq('a, 'b, 'c), Seq('a, 'b, 'd), Seq('a, 'c, 'd), Seq('b, 'c, 'd)),
      Seq(Seq('a, 'b, 'c, 'd)),
      Nil
    )
  }

  test("2 and 2") {
    check('a, 'a, 'b, 'b)(
      Seq(Nil),
      Seq(Seq('a), Seq('a), Seq('b), Seq('b)),
      Seq(Seq('a, 'a), Seq('a, 'b), Seq('a, 'b), Seq('a, 'b), Seq('a, 'b), Seq('b, 'b)),
      Seq(Seq('a, 'a, 'b), Seq('a, 'a, 'b), Seq('a, 'b, 'b), Seq('a, 'b, 'b)),
      Seq(Seq('a, 'a, 'b, 'b)),
      Nil
    )
  }
}
