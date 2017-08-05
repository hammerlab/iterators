package org.hammerlab.iterator.bulk

import org.hammerlab.iterator.SimpleBufferedIterator

/**
 * Some smarter bulk operations on [[BufferedIterator]]s
 */
case class BufferedBulkIterator[T](it: BufferedIterator[T]) {
  def takewhile(fn: T ⇒ Boolean): SimpleBufferedIterator[T] =
    new SimpleBufferedIterator[T] {
      override protected def _advance: Option[T] =
        if (it.hasNext && fn(it.head))
          Some(it.next)
        else
          None
    }

  def dropwhile(fn: T ⇒ Boolean): Unit =
    while (it.hasNext && fn(it.head))
      it.next

  def collectwhile[U](pf: PartialFunction[T, U]): BufferedIterator[U] =
    new SimpleBufferedIterator[U] {
      override protected def _advance: Option[U] =
        if (it.hasNext && pf.isDefinedAt(it.head))
          Some(
            pf(
              it.next
            )
          )
        else
          None
    }
}

object BufferedBulkIterator {
  implicit def makeBufferedBulkWhileIterator[T](it: BufferedIterator[T]): BufferedBulkIterator[T] =
    BufferedBulkIterator(it)
}
