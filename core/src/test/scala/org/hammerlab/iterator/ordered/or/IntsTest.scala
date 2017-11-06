package org.hammerlab.iterator.ordered.or

import hammerlab.or._
import org.hammerlab.iterator.ordered.IdentityIntConversions
import org.hammerlab.iterator.ordered

class IntsTest
  extends Suite
    with ordered.IntsTest
    with IdentityIntConversions {

  override def expected: Map[String, Seq[Or[Int, Int]]] =
    Map(
      "1,2,3 4,5,6" →
        Seq(
          L(1),
          L(2),
          L(3),
          R(4),
          R(5),
          R(6)
        ),
      "1,3,5 2,4,6" →
        Seq(
          L(1),
          R(2),
          L(3),
          R(4),
          L(5),
          R(6)
        ),
      "1,2,3 1,2,3" →
        Seq(
          B(1),
          B(2),
          B(3)
        ),
      "1,2,4,7,9 1,3,5,6,7,8" →
        Seq(
          B(1),
          L(2),
          R(3),
          L(4),
          R(5),
          R(6),
          B(7),
          R(8),
          L(9)
        ),
      "empty empty" → Nil,
      "empty 1" → Seq(R(1)),
      "empty 1,10,100" →
        Seq(
          R(1),
          R(10),
          R(100)
        ),
      "1 empty" → Seq(L(1)),
      "1,10,100 empty" →
        Seq(
          L(1),
          L(10),
          L(100)
        )
    )
}
