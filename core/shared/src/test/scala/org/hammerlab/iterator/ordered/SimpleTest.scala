package org.hammerlab.iterator.ordered

import hammerlab.iterator._

/**
 * These are primarily sanity-checks that reasonable calls compile, given complicated implicit resolution of [[View]]s
 * and [[Ordering]]s
 */
class SimpleTest
  extends org.hammerlab.Suite {

  val l1 = Seq(1, 3, 4)
  val l2 = Seq(2, 3, 5, 6)

  test("ints") {
    {
      import hammerlab.either._
      l1.eitherMerge(l2).toList should be(
        List(
          L(1),
          R(2),
          L(3),
          R(3),
          L(4),
          R(5),
          R(6)
        )
      )
    }

    {
      import hammerlab.or._
      l1.orMerge(l2).toList should be(
        List(
          L(1),
          R(2),
          Both(3, 3),
          L(4),
          R(5),
          R(6)
        )
      )
    }

    l1.leftMerge(l2).mapValues(_.toList).toList should be(
      List(
        1 → Seq(2),
        3 → Seq(3),
        4 → Seq(5, 6)
      )
    )

    l1.merge(l2).toList should be(
      List(
        1, 2, 3, 3, 4, 5, 6
      )
    )
  }

  val s1 = Seq('a → 1, 'b → 3, 'c → 4)

  test("left tuples") {
    implicit val view = View[(Symbol, Int), Int](_._2)

    {
      import hammerlab.either._
      s1.eitherMerge(l2).toList should be(
        List(
          L('a → 1),
          R(2),
          L('b → 3),
          R(3),
          L('c → 4),
          R(5),
          R(6)
        )
      )
    }

    s1.leftMerge(l2).mapValues(_.toList).toList should be(
      List(
        'a → 1 → Seq(2),
        'b → 3 → Seq(3),
        'c → 4 → Seq(5, 6)
      )
    )

    s1.merge(Seq('d → 2, 'e → 3, 'f → 5, 'g → 6)).toList should be(
      List(
        'a → 1,
        'd → 2,
        'b → 3,
        'e → 3,
        'c → 4,
        'f → 5,
        'g → 6
      )
    )
  }

}
