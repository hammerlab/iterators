package org.hammerlab.iterator.start

import hammerlab.iterator.macros.IteratorOps
import org.hammerlab.iterator.SimpleIterator

import scala.collection.mutable
import scala.reflect.ClassTag

/**
 * Some smarter bulk operations on [[BufferedIterator]]s, consuming exactly the elements necessary and not more
 */
@IteratorOps
class BufferedOps[T](it: BufferedIterator[T]) {
  def takewhile(fn: T ⇒ Boolean): SimpleIterator[T] =
    new SimpleIterator[T] {
      override protected def _advance: Option[T] =
        if (it.hasNext && fn(it.head))
          Some(it.next)
        else
          None
    }

  def dropwhile(fn: T ⇒ Boolean): Unit =
    while (it.hasNext && fn(it.head))
      it.next

  def collectwhile[U](pf: PartialFunction[T, U]): BufferedIterator[U] =
    new SimpleIterator[U] {
      override protected def _advance: Option[U] =
        if (it.hasNext && pf.isDefinedAt(it.head))
          Some(
            pf(
              it.next
            )
          )
        else
          None
    }
}


