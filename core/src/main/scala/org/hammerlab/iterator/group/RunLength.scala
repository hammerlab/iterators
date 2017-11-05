package org.hammerlab.iterator.group

import hammerlab.iterator.macros.IteratorWrapper
import spire.implicits._
import spire.math.Integral

/**
 * Run-length encode an input iterator, replacing contiguous runs of identical elements with pairs consisting of the
 * first element in the run and the number of elements observed.
 *
 * See RunLengthIteratorTest for examples.
 */
@IteratorWrapper
class RunLength[K](it: BufferedIterator[K]) {
  def runLengthEncode(implicit ord: Ordering[K]): Iterator[(K, Int)] =
    runLengthEncode(ord.equiv(_, _))

  def runLengthEncode(cmpFn: (K, K) ⇒ Boolean = (_ == _)): Iterator[(K, Int)] =
    new Iterator[(K, Int)] {
      override def hasNext: Boolean = it.hasNext

      override def next(): (K, Int) = {
        val elem = it.head
        var count = 0
        while (it.hasNext && cmpFn(it.head, elem)) {
          it.next()
          count += 1
        }
        (elem, count)
      }
    }
}

@IteratorWrapper
class RunLengthReencode[K, V: Integral](it: BufferedIterator[(K, V)]) {
  def reencode: Iterator[(K, V)] =
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
