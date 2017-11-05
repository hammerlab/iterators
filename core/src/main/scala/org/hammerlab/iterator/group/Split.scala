package org.hammerlab.iterator.group

import hammerlab.iterator.macros.IteratorWrapper

import scala.collection.mutable.ArrayBuffer

/**
 * For each element in an input [[Iterator]], emit the sequence of elements from the current one to the next `sentinel`
 * value (or the end of the iterator, if no more sentinels follow the current element).
 *
 * See `SplitTest` for examples.
 */
@IteratorWrapper
class Split[T](it: Iterator[T]) {
  def splitBy(sentinel: T): Iterator[Seq[T]] =
    new Iterator[Seq[T]] {
      val buf: ArrayBuffer[T] = ArrayBuffer()
      var idx = 0

      var padded = false
      def fill(throwIfCant: Boolean = false): Unit = {
        buf.clear()
        idx = 0

        if (throwIfCant && !it.hasNext) {
          throw new NoSuchElementException()
        }
        var filling = true
        while (it.hasNext && filling) {
          val next = it.next()
          buf.append(next)
          if (next == sentinel)
            filling = false
        }
        if (!it.hasNext && filling) {
          buf.append(sentinel)
          padded = true
        }
      }

      override def hasNext: Boolean = {
        idx + (if (padded) 1 else 0) < buf.length || it.hasNext
      }

      override def next(): Seq[T] = {
        if (idx == buf.length) {
          fill(true)
        }
        val ret = buf.view(idx, buf.length - 1)
        idx += 1
        ret
      }
    }
}
