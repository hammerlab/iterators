package org.hammerlab.iterator

case class Sliding2OptIterator[T](it: Iterator[T]) {
  def sliding2: Iterator[(Option[T], T)] =
    new Iterator[(Option[T], T)] {
      var prevOpt: Option[T] = None
      override def hasNext: Boolean = it.hasNext
      override def next(): (Option[T], T) = {
        val cur = it.next()
        val ret = (prevOpt, cur)
        prevOpt = Some(cur)
        ret
      }
    }
}

object Sliding2OptIterator {
  implicit def makeSliding2OptIterator[T](it: Iterator[T]): Sliding2OptIterator[T] = Sliding2OptIterator(it)
}
