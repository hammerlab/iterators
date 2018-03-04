package org.hammerlab.iterator.map

import hammerlab.iterator.macros.IteratorOps

@IteratorOps
class MapValues[K, V](it: Iterator[(K, V)]) {
  def mapValues[U](fn: V ⇒ U): Iterator[(K, U)] =
    it
      .map {
        case (k, v) ⇒
          k → fn(v)
      }
}
