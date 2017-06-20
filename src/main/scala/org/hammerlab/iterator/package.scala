package org.hammerlab

package object iterator {
  implicit class NextOptionIterator[T](val it: Iterator[T]) extends AnyVal {
    def nextOption: Option[T] =
      if (it.hasNext)
        Some(it.next)
      else
        None
  }

  implicit class HeadOptionIterator[T](val it: BufferedIterator[T]) extends AnyVal {
    def headOption: Option[T] =
      if (it.hasNext)
        Some(it.head)
      else
        None
  }
}
