package org.hammerlab.iterator.sorted.zip

import org.hammerlab.iterator.sorted
import org.hammerlab.iterator.sorted.VInt
import org.hammerlab.iterator.sorted.ZipIterator._

class IntsTest
  extends sorted.Suite
    with VInt
    with sorted.IntsTest {

  override type Result = Int

  override def check(left: Seq[L])(right: Seq[R])(expected: Seq[Int]): Unit =
    left
      .sortedZip(right)
      .toList should be(
      expected
    )

  override def L(t: L): Result = t
  override def R(u: R): Result = u

  override def expected: Map[String, Seq[Result]] =
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
          L(1),
          R(1),
          L(2),
          R(2),
          L(3),
          R(3)
        ),
      "1,2,4,7,9 1,3,5,6,7,8" →
        Seq(
          L(1),
          R(1),
          L(2),
          R(3),
          L(4),
          R(5),
          R(6),
          L(7),
          R(7),
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
