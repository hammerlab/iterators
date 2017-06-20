package org.hammerlab.iterator

import scala.collection.mutable

case class CountIteratorElems[T](it: Iterator[T]) {
  def countElems: Map[T, Int] = {
    val counts = mutable.Map[T, Int]()
    for {
      elem ← it
    } {
      counts.update(elem, counts.getOrElse(elem, 0) + 1)
    }
    counts.toMap
  }
}

object CountIteratorElems {
  implicit def makeCountIteratorElems[T](it: Iterator[T]): CountIteratorElems[T] = CountIteratorElems(it)
}
