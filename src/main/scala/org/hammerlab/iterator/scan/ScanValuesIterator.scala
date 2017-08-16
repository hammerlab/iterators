package org.hammerlab.iterator.scan

import cats.Monoid
import org.hammerlab.iterator.DropRightIterator._

trait HasEmpty[K] {
  protected var k: K = _
}

case class ScanValuesIterator[K, V](it: Iterator[(K, V)])
  extends HasEmpty[K] {

  def scanLeftValues(implicit m: Monoid[V]): Iterator[(K, V)] =
    scanLeftValues(m.empty, m.combine)

  def scanLeftValues[U](identity: U, combine: (U, V) ⇒ U): Iterator[(K, U)] =
    it
      .scanLeft(
        k → identity
      ) {
        case ((_, u), (k, v)) ⇒
          k →
            combine(u, v)
      }
      .drop(1)

  def scanRightValues(implicit m: Monoid[V]): Iterator[(K, V)] =
    scanRightValues(m.empty, m.combine)

  def scanRightValues[U](identity: U, combine: (V, U) ⇒ U): Iterator[(K, U)] =
    it
      .scanRight(
        k → identity
      ) {
        case ((k, v), (_, u)) ⇒
          k →
            combine(v, u)
      }
      .dropRight(1)
}

object ScanValuesIterator {
  implicit def makeScanValuesIterator[K, V](it: Iterator[(K, V)]): ScanValuesIterator[K, V] = ScanValuesIterator(it)
  implicit def makeScanValuesIterator[K, V](it: Iterable[(K, V)]): ScanValuesIterator[K, V] = ScanValuesIterator(it.iterator)
  implicit def makeScanValuesIterator[K, V](it: Array[(K, V)]): ScanValuesIterator[K, V] = ScanValuesIterator(it.iterator)
}
