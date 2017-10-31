package org.hammerlab.iterator.scan

import cats.Monoid
import org.hammerlab.iterator.DropRightIterator._

trait Scan {
  implicit class ScanOps[T](it: Iterator[T]) extends Serializable {

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
        scanned.dropRight(1)
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
        scanned.dropRight(1)
      else
        scanned.drop(1)
    }
  }

  implicit def makeScanIterator[T](it: Iterable[T]): ScanOps[T] = new ScanOps(it.iterator)
  implicit def makeScanIterator[T](it: Array[T]): ScanOps[T] = new ScanOps(it.iterator)
}
