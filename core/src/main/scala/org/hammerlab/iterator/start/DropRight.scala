package org.hammerlab.iterator.start

import hammerlab.iterator.macros.IteratorOps

import scala.collection.mutable

@IteratorOps
class DropRight[T](it: Iterator[T]) {
  def dropright(n: Int): Iterator[T] =
    if (n == 0)
      it
    else if (n < 0)
      throw new IllegalArgumentException(s"Can't dropRight() a negative number of elements")
    else
      new Iterator[T] {

        private val buf = mutable.Queue[T]()

        override def hasNext: Boolean = {
          while (buf.length < n && it.hasNext) {
            buf += it.next()
          }
          buf.length == n && it.hasNext
        }

        override def next(): T = {
          if (!hasNext)
            throw new NoSuchElementException

          buf.dequeue()
        }
      }
}
