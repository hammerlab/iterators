package org.hammerlab.iterator

case class FinishingIterator[T](it: Iterator[T]) {
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

object FinishingIterator {
  implicit def makeFinallyIterator[T](it: Iterator[T]): FinishingIterator[T] =
    FinishingIterator(it)
}
