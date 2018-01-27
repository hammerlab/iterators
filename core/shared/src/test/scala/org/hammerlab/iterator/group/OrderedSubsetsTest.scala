package org.hammerlab.iterator.group

import hammerlab.iterator.group._
import org.hammerlab.Suite

class OrderedSubsetsTest
  extends Suite {

  def check[T](elems: T*)(expecteds: Seq[Seq[T]]*): Unit =
    for {
      (expected, n) ← expecteds.zipWithIndex
    } {
      elems
        .orderedSubsetsWithReplacement(n)
        .toList should be(
        expected
      )
    }

  test("empty") {
    check()(Seq(Nil), Seq(Nil), Seq(Nil), Seq(Nil))
  }

  test("singleton") {
    check('a)(
      Seq(Seq()),
      Seq(Seq('a)),
      Seq(Seq('a, 'a)),
      Seq(Seq('a, 'a, 'a))
    )
  }

  test("2 elems") {
    check(
      'a, 'b
    )(
      Seq(Seq()),
      Seq(Seq('a), Seq('b)),
      Seq(
        Seq('a, 'a), Seq('a, 'b),
        Seq('b, 'a), Seq('b, 'b)
      ),
      Seq(
        Seq('a, 'a, 'a), Seq('a, 'a, 'b), Seq('a, 'b, 'a), Seq('a, 'b, 'b),
        Seq('b, 'a, 'a), Seq('b, 'a, 'b), Seq('b, 'b, 'a), Seq('b, 'b, 'b)
      )
    )
  }

  test("3 elems") {
    check(
      'a, 'b, 'c
    )(
      Seq(Seq()),
      Seq(Seq('a), Seq('b), Seq('c)),
      Seq(
        Seq('a, 'a), Seq('a, 'b), Seq('a, 'c),
        Seq('b, 'a), Seq('b, 'b), Seq('b, 'c),
        Seq('c, 'a), Seq('c, 'b), Seq('c, 'c)
      ),
      Seq(
        Seq('a, 'a, 'a), Seq('a, 'a, 'b), Seq('a, 'a, 'c),
        Seq('a, 'b, 'a), Seq('a, 'b, 'b), Seq('a, 'b, 'c),
        Seq('a, 'c, 'a), Seq('a, 'c, 'b), Seq('a, 'c, 'c),

        Seq('b, 'a, 'a), Seq('b, 'a, 'b), Seq('b, 'a, 'c),
        Seq('b, 'b, 'a), Seq('b, 'b, 'b), Seq('b, 'b, 'c),
        Seq('b, 'c, 'a), Seq('b, 'c, 'b), Seq('b, 'c, 'c),

        Seq('c, 'a, 'a), Seq('c, 'a, 'b), Seq('c, 'a, 'c),
        Seq('c, 'b, 'a), Seq('c, 'b, 'b), Seq('c, 'b, 'c),
        Seq('c, 'c, 'a), Seq('c, 'c, 'b), Seq('c, 'c, 'c)
      )
    )
  }
}
