package org.hammerlab.iterator

case class DropEagerIterator[T](it: Iterator[T]) {
  def dropEager(n: Int): Iterator[T] = {
    var idx = 0
    while (it.hasNext && idx < n) {
      it.next
      idx += 1
    }
    it
  }
}

object DropEagerIterator {
  implicit def makeDropEagerIterator[T](it: Iterator[T]): DropEagerIterator[T] =
    DropEagerIterator(it)
}
