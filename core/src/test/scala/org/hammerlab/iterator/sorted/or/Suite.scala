package org.hammerlab.iterator.sorted.or

import hammerlab.either._
import org.hammerlab.iterator.sorted
import org.hammerlab.iterator.sorted.ConvertToInt
import org.hammerlab.iterator.sorted.OrZipIterator._
import org.hammerlab.test.matchers.seqs.SeqMatcher.seqMatch

abstract class Suite
  extends sorted.Suite
    with ConvertToInt {

  type Result = Or[L, R]

  override def check(left: Seq[L])(right: Seq[R])(expected: Seq[Result]): Unit = {
    left
      .iterator
      .sortedOrZip(right.iterator)
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
