package org.hammerlab.iterator.start

import hammerlab.iterator.macros.IteratorOps

@IteratorOps
class HeadOption[T](it: BufferedIterator[T]) {
  def headOption: Option[T] =
    if (it.hasNext)
      Some(it.head)
    else
      None
}
