package org.hammerlab.iterator.end

import hammerlab.iterator.macros.IteratorWrapper
import org.hammerlab.iterator.SimpleIterator

@IteratorWrapper
class ExpandLastElement[T](it: Iterator[T]) {
  def expandLastElement(fn: T ⇒ Iterator[T]): Iterator[T] = {
    val main =
      new SimpleIterator[T] {
        var lastOpt: Option[T] = None

        override protected def _advance: Option[T] =
          if (it.hasNext) {
            lastOpt = Some(it.next)
            lastOpt
          } else
            None
      }

    main ++
      new Iterator[T] {
        lazy val rest =
          main
            .lastOpt
            .map(fn)
            .getOrElse(Iterator())

        override def hasNext: Boolean = rest.hasNext
        override def next(): T = rest.next
      }
  }
}
