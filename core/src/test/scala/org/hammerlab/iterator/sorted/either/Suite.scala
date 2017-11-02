package org.hammerlab.iterator.sorted.either

import org.hammerlab.iterator.sorted
import org.hammerlab.iterator.sorted.ConvertToInt
import org.hammerlab.iterator.sorted.EitherZipIterator._

abstract class Suite
  extends sorted.Suite
    with ConvertToInt {

  type Result = Either[L, R]

  override def check(left: Seq[L])(right: Seq[R])(expected: Seq[Result]): Unit = {
    left
      .sortedEitherZip(right)
      .toList should be(
        expected
      )
  }

  def L(l: L): Result = Left(l)
  def R(r: R): Result = Right(r)
}
