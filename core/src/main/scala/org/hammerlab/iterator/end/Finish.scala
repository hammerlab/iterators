package org.hammerlab.iterator.end

import hammerlab.iterator.macros.IteratorWrapper
import org.hammerlab.iterator.SimpleBufferedIterator

@IteratorWrapper
class Finish[T](it: Iterator[T]) {
  def finish(fn: ⇒ Unit): Iterator[T] =
    new SimpleBufferedIterator[T] {
      override protected def _advance: Option[T] =
        if (it.hasNext)
          Some(it.next)
        else
          None

      override protected def done(): Unit = {
        fn
      }
    }
}
