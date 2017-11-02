package org.hammerlab.iterator

import scala.collection.mutable
import scala.reflect.ClassTag

case class TakeEagerIterator[T: ClassTag](it: Iterator[T]) {
  def takeEager(n: Int): Array[T] = {
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

object TakeEagerIterator {
  implicit def makeTakeEagerIterator[T: ClassTag](it: Iterator[T]): TakeEagerIterator[T] =
    TakeEagerIterator(it)
}
