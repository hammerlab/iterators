package org.hammerlab.iterator.sliding

import hammerlab.iterator.macros.IteratorWrapper
import org.hammerlab.iterator.SimpleIterator

import scala.collection.mutable.ArrayBuffer

@IteratorWrapper
class Sliding[T](it: Iterator[T]) {
  def slide(n: Int): Iterator[Seq[T]] =
    new SimpleIterator[Seq[T]] {
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
