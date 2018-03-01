package org.hammerlab.iterator.ordered.or

import hammerlab.iterator.ordered._
import hammerlab.or._
import org.hammerlab.iterator.ordered
import org.hammerlab.iterator.ordered.ConvertToInt

abstract class Suite
  extends ordered.Suite
    with ConvertToInt {

  type Result = Or[L, R]

  override def check(left: Seq[L])(right: Seq[R])(expected: Seq[Result]): Unit = {
    left
      .iterator
      .orMerge(right.iterator)
      .toList should seqMatch(
        expected
      )
  }

  def B(l: L)(implicit ev: L =:= R): Result = Both(l, l)
  def B(l: L, r: R): Result = Both(l, r)

  import cats.data.Ior._
  def L(l: L): Result = Left(l)
  def R(r: R): Result = Right(r)
}
