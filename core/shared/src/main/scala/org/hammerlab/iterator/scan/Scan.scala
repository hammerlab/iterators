package org.hammerlab.iterator.scan

import cats.Monoid
import hammerlab.iterator._
import hammerlab.iterator.macros.IteratorOps

@IteratorOps
class Scan[T](it: Iterator[T]) {

  def scanL(implicit m: Monoid[T]): Iterator[T] = scanL(includeCurrentValue = false)
  def scanLeftInclusive(implicit m: Monoid[T]): Iterator[T] = scanL(includeCurrentValue = true)

  def scanL(includeCurrentValue: Boolean)(
      implicit m: Monoid[T]
  ): Iterator[T] = {
    val scanned =
      it
        .scanLeft(
          m.empty
        )(
          m.combine
        )

    if (includeCurrentValue)
      scanned.drop(1)
    else
      scanned.dropright(1)
  }

  def scanR(implicit m: Monoid[T]): Iterator[T] = scanR(includeCurrentValue = false)
  def scanRightInclusive(implicit m: Monoid[T]): Iterator[T] = scanR(includeCurrentValue = true)

  def scanR(includeCurrentValue: Boolean)(
      implicit m: Monoid[T]
  ): Iterator[T] = {
    val scanned =
      it
        .scanRight(
          m.empty
        )(
          m.combine
        )

    if (includeCurrentValue)
      scanned.dropright(1)
    else
      scanned.drop(1)
  }
}
