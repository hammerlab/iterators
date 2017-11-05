package org.hammerlab.iterator.start

import hammerlab.iterator.macros.IteratorOps

@IteratorOps
class NextOption[T](it: Iterator[T]) {
  def nextOption: Option[T] =
    if (it.hasNext)
      Some(it.next)
    else
      None
}
