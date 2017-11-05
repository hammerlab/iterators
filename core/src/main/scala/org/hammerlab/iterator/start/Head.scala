package org.hammerlab.iterator.start

trait Head {
  implicit class NextOption[T](it: Iterator[T]) {
    def nextOption: Option[T] =
      if (it.hasNext)
        Some(it.next)
      else
        None
  }

  implicit class HeadOption[T](it: BufferedIterator[T]) {
    def headOption: Option[T] =
      if (it.hasNext)
        Some(it.head)
      else
        None
  }
}
