package org.hammerlab.iterator

import hammerlab.iterator.macros.IteratorOps
import hammerlab.iterator.start._

import scala.reflect.ClassTag
import scala.util.Random

/**
 * Sample elements from an iterator into an [[Array]]
 */
@IteratorOps
class Sample[T](it: Iterator[T]) {
  def sample(n: Int)(implicit ct: ClassTag[T]): Array[T] = {
    if (n == 0)
      return Array()

    val zipped = it.zipWithIndex
    val arr = zipped.takeEager(n)
    while (it.hasNext) {
      val (elem, idx) = zipped.next()
      val r = Random.nextInt(idx)
      if (r < n) {
        arr(r) = (elem, idx)
      }
    }

    arr
      .sortBy(_._2)
      .map(_._1)
  }
}
