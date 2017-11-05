package org.hammerlab.iterator

import hammerlab.iterator.start._

import scala.reflect.ClassTag
import scala.util.Random

/**
 * Sample elements from an iterator into an [[Array]]
 */
case class SamplingIterator[T: ClassTag](it: Iterator[T]) {
  def sample(n: Int): Array[T] = {
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

object SamplingIterator {
  implicit def makeSamplingIterator[T: ClassTag](it: Iterator[T]): SamplingIterator[T] =
    SamplingIterator(it)
}
