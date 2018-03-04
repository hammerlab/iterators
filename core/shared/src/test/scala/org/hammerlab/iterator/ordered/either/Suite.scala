package org.hammerlab.iterator.ordered.either

import hammerlab.iterator.ordered._
import org.hammerlab.iterator.ordered.ConvertToInt
import org.hammerlab.iterator.ordered

abstract class Suite
  extends ordered.Suite
    with ConvertToInt {

  type Result = Either[L, R]

  override def check(left: Seq[L])(right: Seq[R])(expected: Seq[Result]): Unit = {
    left
      .eitherMerge(right)
      .toList should be(
        expected
      )
  }

  def L(l: L): Result = Left(l)
  def R(r: R): Result = Right(r)
}
