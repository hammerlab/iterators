package org.hammerlab.iterator.util

import hammerlab.iterator._
import org.hammerlab.iterator.SimpleBufferedIterator

abstract class MapWithStateIterator[T, U](it: Iterator[T])
  extends SimpleBufferedIterator[U] {

  def fn(t: T): U

  override protected def _advance: Option[U] =
    it
      .nextOption
      .map(fn)
}

abstract class MapValuesWithStateIterator[K, V, W](it: Iterator[(K, V)])
  extends MapWithStateIterator[(K, V), (K, W)](it) {

  def w(v: V): W

  override def fn(t: (K, V)): (K, W) =
    t match {
      case (k, v) ⇒
        k → w(v)
    }
}
