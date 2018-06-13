package org.hammerlab.iterator.sliding

import hammerlab.iterator.macros.IteratorOps
import org.hammerlab.iterator.util.SimpleIterator

@IteratorOps
class Sliding2[T](it: BufferedIterator[T]) {
  def sliding2Prev: Iterator[(Option[T], T)] =
    new BufferedIterator[(Option[T], T)] {
      var prevOpt: Option[T] = None
      def hasNext: Boolean = it.hasNext
      def head: (Option[T], T) = (prevOpt, it.head)
      def next(): (Option[T], T) = {
        val r = (prevOpt, it.next())
        prevOpt = Some(r._2)
        r
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

  def sliding2(pad: T): Iterator[(T, T)] =
    sliding2Opt
      .map {
        case (elem, succOpt) ⇒
          elem → succOpt.getOrElse(pad)
      }

  def sliding2: Iterator[(T, T)] =
    new SimpleIterator[(T, T)] {
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
