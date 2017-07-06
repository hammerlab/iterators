package org.hammerlab.iterator.sorted

import org.hammerlab.iterator.sorted.OrZipIterator._
import org.hammerlab.types.{ Both, LO, Or, RO }

abstract class OrZip
  extends ZipIteratorTest
    with EitherOr {

  type Result = Or[L, R]

  override def check(left: L*)(right: R*)(expected: Result*): Unit = {
    left
      .iterator
      .sortedOrZip(right.iterator)
      .toList should be(
        expected
      )
  }

  def B(l: L)(implicit ev: L =:= R): Result = Both(l, l)
  def B(l: L, r: R): Result = Both(l, r)

  def L(l: L): Result = LO(l)
  def R(r: R): Result = RO(r)
}

class OrInts
  extends OrZip
    with ZipIntsTest {

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
