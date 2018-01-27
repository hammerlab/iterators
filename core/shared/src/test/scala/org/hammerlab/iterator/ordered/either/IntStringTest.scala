package org.hammerlab.iterator.ordered.either

import org.hammerlab.iterator.ordered

class IntStringTest
  extends Suite
    with ordered.IntStringTest {
  override def expected: Seq[Either[L, R]] =
    Seq(
      R(""),
      L(1),
      R("a"),
      R("a"),
      L(2),
      R("bb"),
      R("ccc"),
      L(4),
      R("eeeee"),
      R("ffffff"),
      L(7),
      L(10),
      L(11),
      L(11),
      R("kkkkkkkkkkk"),
      R("nnnnnnnnnnnnnn"),
      L(15)
    )
}
