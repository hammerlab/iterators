package org.hammerlab.iterator.group

import cats.Eq
import hammerlab.iterator.macros.IteratorOps
import spire.implicits._
import spire.math.Integral

trait Cmp[T] {
  def apply(l: T, r: T): Boolean
}
trait LowPriCmp {
  def apply[T](fn: (T, T) ⇒ Boolean): Cmp[T] =
    new Cmp[T] {
      @inline override def apply(l: T, r: T): Boolean = fn(l, r)
    }
  implicit def fromOrd[T](implicit o: Ordering[T]): Cmp[T] = Cmp(o.equiv)
}
object Cmp extends LowPriCmp {
  implicit def fromEq[T](implicit e: Eq[T]): Cmp[T] = Cmp(e.eqv)
}

/**
 * Run-length encode an input iterator, replacing contiguous runs of identical elements with pairs consisting of the
 * first element in the run and the number of elements observed.
 *
 * See RunLengthIteratorTest for examples.
 */
@IteratorOps
class RunLength[K](it: BufferedIterator[K]) {
  def runLengthEncode(implicit cmp: Cmp[K]): Iterator[(K, Int)] =
    runLengthEncode(
      ((cur: K), (next: K)) ⇒
        if (cmp(cur, next))
          Some(cur)
        else
          None
    )

  def runLengthEncode(cmpFn: (K, K) ⇒ Option[K]): Iterator[(K, Int)] =
    new Iterator[(K, Int)] {
      override def hasNext: Boolean = it.hasNext
      override def next(): (K, Int) = {
        var elem = it.next
        var count = 1
        var cmpResult: Option[K] = Some(elem)
        while (it.hasNext && cmpResult.isDefined) {
          cmpResult = cmpFn(elem, it.head)
          cmpResult.foreach {
            r ⇒
              elem = r
              count += 1
              it.next
          }
        }
        (elem, count)
      }
    }
}

@IteratorOps
class RunLengthReencode[K, V](it: BufferedIterator[(K, V)]) {
  def runLengthReencode(implicit ev: Integral[V]): Iterator[(K, V)] =
    new Iterator[(K, V)] {
      override def hasNext: Boolean = it.hasNext

      override def next(): (K, V) = {
        var ret = it.next()
        while (it.hasNext && it.head._1 == ret._1) {
          ret = (ret._1, ret._2 + it.next()._2)
        }
        ret
      }
    }

}
