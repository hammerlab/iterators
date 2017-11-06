package org.hammerlab.iterator.ordered

import hammerlab.iterator._
import hammerlab.iterator.macros.IteratorOps

/**
 * Group one sorted iterator with another, emitting an iterator of the latter's elements for each of the former's
 */
@IteratorOps
class LeftMerge[T](it: BufferedIterator[T]) {
  def leftMerge[U, V](other: Iterable[U])(
      implicit
      tv: View[T, V],
      uv: View[U, V],
      ord: Ordering[V]
  ): Iterator[(T, Iterator[U])] =
    leftMerge(other.iterator)

  def leftMerge[U, V](other: Iterator[U])(
      implicit
      tv: View[T, V],
      uv: View[U, V],
      ord: Ordering[V]
  ): Iterator[(T, Iterator[U])] = {
    val o = other.buffered
    val ≥ = ord.gteq _
    it.map {
      t ⇒
        val nextV = it.headOption.map(tv)
        t →
          o
            .takewhile {
              u ⇒
                // If the next bound exists, only take [[U]]s that are less than it
                !nextV.exists(
                  ≥(uv(u), _)
                )
            }
    }
  }
}
