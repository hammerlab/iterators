package org.hammerlab.iterator

import scala.collection.mutable

case class CountIteratorByKey[K, V](it: Iterator[(K, V)]) {
  def countByKey: Map[K, Int] = {
    val counts = mutable.Map[K, Int]()
    for {
      (k, _) ← it
    } {
      counts.update(k, counts.getOrElse(k, 0) + 1)
    }
    counts.toMap
  }
}

object CountIteratorByKey {
  implicit def makeCountIteratorByKey[K, V](it: Iterator[(K, V)]): CountIteratorByKey[K, V] = CountIteratorByKey(it)
}
