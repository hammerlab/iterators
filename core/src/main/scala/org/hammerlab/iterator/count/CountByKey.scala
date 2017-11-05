package org.hammerlab.iterator.count

import hammerlab.iterator.macros.IteratorOps

import scala.collection.mutable

@IteratorOps
class CountByKey[K, V](it: Iterator[(K, V)]) {
  def countByKey: Map[K, Int] = {
    val counts = mutable.Map[K, Int]()
    it.foreach {
      case (k, _) ⇒
        counts.update(
          k,
          counts.getOrElse(k, 0) + 1
        )
    }
    counts.toMap
  }
}
