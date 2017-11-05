package org.hammerlab.iterator.start

import hammerlab.iterator.macros.IteratorOps

import scala.collection.mutable
import scala.reflect.ClassTag

/**
 * Exposes `.takeEager` on iterators, which is identical to `.take.toArray`, but always consumes the taken elements from
 * the original iterator.
 *
 * Some stdlib iterators backed by [[Seq]]s have `.take` implementations that create a view over the "taken" elements,
 * so that the original iterator is not consumed when the taken/prefix iterator is materialized; this homogenizes that
 * behavior.
 */
@IteratorOps
class TakeEager[T](it: Iterator[T]) {
  def takeEager(n: Int)(implicit ct: ClassTag[T]): Array[T] = {
    val builder = mutable.ArrayBuilder.make[T]()
    for {
      _ ← 0 until n
      if it.hasNext
    } {
      builder += it.next
    }
    builder.result
  }
}
