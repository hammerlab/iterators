package org.hammerlab.iterator

case class BufferedTakeWhileIterator[T](it: BufferedIterator[T]) {
  def takewhile(fn: T ⇒ Boolean): BufferedIterator[T] =
    new SimpleBufferedIterator[T] {
      override protected def _advance: Option[T] =
        if (it.hasNext && fn(it.head))
          Some(it.next)
        else
          None
    }
}

object BufferedTakeWhileIterator {
  implicit def makeBufferedTakeWhileIterator[T](it: BufferedIterator[T]): BufferedTakeWhileIterator[T] =
    BufferedTakeWhileIterator(it)
}
