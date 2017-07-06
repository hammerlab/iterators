package org.hammerlab.iterator.sorted

import org.hammerlab.iterator.sorted.EitherZipIterator._

abstract class EitherZip
  extends ZipIteratorTest
    with EitherOr {

  type Result = Either[L, R]

  override def check(left: L*)(right: R*)(expected: Result*): Unit = {
    left
      .sortedEitherZip(right)
      .toList should be(
        expected
      )
  }

  def L(l: L): Result = Left(l)
  def R(r: R): Result = Right(r)
}
