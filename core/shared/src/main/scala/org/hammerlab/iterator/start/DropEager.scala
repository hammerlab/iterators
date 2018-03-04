package org.hammerlab.iterator.start

import hammerlab.iterator.macros.IteratorOps

@IteratorOps
class DropEager[T](it: Iterator[T]) {
  def dropEager(n: Int): Iterator[T] = {
    var idx = 0
    while (it.hasNext && idx < n) {
      it.next
      idx += 1
    }
    it
  }
}
