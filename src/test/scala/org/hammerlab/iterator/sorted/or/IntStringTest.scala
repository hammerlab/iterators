package org.hammerlab.iterator.sorted.or

import hammerlab.either._
import org.hammerlab.iterator.sorted

class IntStringTest
  extends Suite
    with sorted.IntStringTest {
  override def expected: Seq[Or[L, R]] =
    Seq(
      R(""),
      B(1, "a"),
      R("a"),
      B(2, "bb"),
      R("ccc"),
      L(4),
      R("eeeee"),
      R("ffffff"),
      L(7),
      L(10),
      B(11, "kkkkkkkkkkk"),
      L(11),
      R("nnnnnnnnnnnnnn"),
      L(15)
    )
}
