package org.hammerlab.iterator.scan

import cats.Monoid
import org.hammerlab.iterator.DropRightIterator._

case class ScanIterator[T](it: Iterator[T]) {
  def scanL(implicit m: Monoid[T]): Iterator[T] =
    it
      .scanLeft(
        m.empty
      )(
        m.combine
      )
      .drop(1)

  def scanR(implicit m: Monoid[T]): Iterator[T] =
    it
      .scanRight(
        m.empty
      )(
        m.combine
      )
      .dropRight(1)
}

object ScanIterator {
  implicit def makeScanIterator[T](it: Iterator[T]): ScanIterator[T] = ScanIterator(it)
  implicit def makeScanIterator[T](it: Iterable[T]): ScanIterator[T] = ScanIterator(it.iterator)
  implicit def makeScanIterator[T](it: Array[T]): ScanIterator[T] = ScanIterator(it.iterator)
}
