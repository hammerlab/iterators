package org.hammerlab

package object iterator {
  implicit class NextOptionIterator[T](val it: Iterator[T]) extends AnyVal {
    def nextOption: Option[T] =
      if (it.hasNext)
        Some(it.next)
      else
        None
  }
}
