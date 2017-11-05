package org.hammerlab.iterator.sliding

import hammerlab.iterator.macros.IteratorWrapper
import org.hammerlab.iterator.SimpleBufferedIterator

import scala.collection.mutable.ArrayBuffer

@IteratorWrapper
class SlidingIterator[T](it: Iterator[T]) {
  def slide(n: Int): Iterator[Seq[T]] =
    new SimpleBufferedIterator[Seq[T]] {
      var bufferOpt: Option[ArrayBuffer[T]] = None
      override protected def _advance: Option[Seq[T]] =
        bufferOpt match {
          case Some(buffer) ⇒
            val newBuffer = buffer.drop(1)

            if (it.hasNext)
              newBuffer += it.next

            bufferOpt = Some(newBuffer)

            if (buffer.nonEmpty)
              Some(buffer)
            else
              None
          case None ⇒
            val buffer = ArrayBuffer[T]()
            for {
              _ ← 0 until n
              if it.hasNext
            } {
              buffer += it.next
            }
            bufferOpt = Some(buffer)
            _advance
        }
    }
}

//object SlidingIterator {
//  implicit def makeSlidingIterator[T](it: Iterator[T]): SlidingIterator[T] = SlidingIterator(it)
//}
