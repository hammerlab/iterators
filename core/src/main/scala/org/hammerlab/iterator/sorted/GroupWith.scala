package org.hammerlab.iterator.sorted

import hammerlab.iterator.start._
import hammerlab.iterator.HeadOption

/**
 * Group one sorted iterator with another, emitting an iterator of the latter's elements for each of the former's
 */
case class GroupWith[T](it: BufferedIterator[T]) {
  def groupWith[U, V: Ordering](other: Iterator[U])(
      implicit
      tv: T ⇒ V,
      uv: U ⇒ V
  ): Iterator[(T, Iterator[U])] = {
    val o = other.buffered
    val ≥ = implicitly[Ordering[V]].gteq _
    for {
      t ← it
      nextV = it.headOption.map(tv)
    } yield
      t →
        o
          .takewhile(
            u ⇒
              // If the next bound exists, only take [[U]]s that are less than it
              !nextV.exists(
                ≥(uv(u), _)
              )
          )
  }
}

object GroupWith {
  implicit def makeGroupByIterator[T](it: Iterator[T]): GroupWith[T] =
    GroupWith(it.buffered)
}
