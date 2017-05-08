package org.hammerlab.iterator

case class Sliding2Iterator[T](it: BufferedIterator[T]) {
  def sliding2Prev: Iterator[(Option[T], T)] =
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

  def sliding2Opt: Iterator[(T, Option[T])] =
    new Iterator[(T, Option[T])] {
      override def hasNext: Boolean = it.hasNext
      override def next(): (T, Option[T]) =
        it.next →
          (
            if (it.hasNext)
              Some(it.head)
            else
              None
          )
    }

  def sliding2: Iterator[(T, T)] =
    new SimpleBufferedIterator[(T, T)] {
      var curOpt: Option[T] = None
      override protected def _advance: Option[(T, T)] =
        curOpt match {
          case None ⇒
            if (it.hasNext) {
              curOpt = Some(it.next)
              _advance
            } else
              None
          case Some(cur) ⇒
            if (it.hasNext) {
              val n = it.next
              curOpt = Some(n)
              Some(cur → n)
            } else
              None
        }
    }
}

object Sliding2Iterator {
  implicit def makeSliding2OptIterator[T](it: Iterator[T]): Sliding2Iterator[T] = Sliding2Iterator(it.buffered)
  implicit def makeSliding2OptIterator[T](seq: Seq[T]): Sliding2Iterator[T] = Sliding2Iterator(seq.iterator.buffered)
}
