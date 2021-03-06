package org.hammerlab.iterator.scan

import cats.Monoid
import hammerlab.iterator._
import hammerlab.iterator.macros.IteratorOps
import org.hammerlab.iterator.util.{ MapValuesWithStateIterator, SimpleIterator }

@IteratorOps
class ScanValues[K, V](it: Iterator[(K, V)]) {

  def scanLeftValues(includeCurrentValue: Boolean)(
      implicit m: Monoid[V]
  ): Iterator[(K, V)] =
    if (includeCurrentValue)
      scanLeftValuesInclusive
    else
      scanLeftValues

  def scanLeftValues(implicit m: Monoid[V]): Iterator[(K, V)] =
    scanLeftValues(
      m.empty
    )(
      m.combine
    )

  def scanLeftValues[U](identity: U)(combine: (U, V) ⇒ U): SimpleIterator[(K, U)] =
    new MapValuesWithStateIterator[K, V, U](it) {
      private var u = identity
      override def w(v: V): U = {
        val ret = u
        u = combine(u, v)
        ret
      }
    }

  def scanLeftValuesInclusive(implicit m: Monoid[V]): Iterator[(K, V)] =
    scanLeftValuesInclusive(
      m.empty
    )(
      m.combine
    )

  def scanLeftValuesInclusive[U](identity: U)(combine: (U, V) ⇒ U): SimpleIterator[(K, U)] =
    new MapValuesWithStateIterator[K, V, U](it) {
      private var u = identity
      override def w(v: V): U = {
        u = combine(u, v)
        u
      }
    }

  def scanRightValues(includeCurrentValue: Boolean)(
      implicit m: Monoid[V]
  ): Iterator[(K, V)] =
    if (includeCurrentValue)
      scanRightValuesInclusive
    else
      scanRightValues

  def scanRightValues(implicit m: Monoid[V]): Iterator[(K, V)] =
    scanRightValues(
      m.empty,
      m.combine
    )

  def scanRightValues[U](identity: U,
                         combine: (V, U) ⇒ U): Iterator[(K, U)] = {
    val k: K = null.asInstanceOf[K]  // unused
    it
      .scanRight(
        k → identity
      ) {
        case ((k, v), (_, u)) ⇒
          k →
            combine(v, u)
      }
      .sliding2
      .map {
        case ((k, _), (_, u)) ⇒
          k → u
      }
  }

  def scanRightValuesInclusive(implicit m: Monoid[V]): Iterator[(K, V)] =
    scanRightValuesInclusive(
      m.empty,
      m.combine
    )

  def scanRightValuesInclusive[U](identity: U,
                                  combine: (V, U) ⇒ U): Iterator[(K, U)] = {
    val k: K = null.asInstanceOf[K]  // unused
    it
      .scanRight(
        k → identity
      ) {
        case ((k, v), (_, u)) ⇒
          k →
            combine(v, u)
      }
      .dropright(1)
  }
}
