package org.hammerlab.iterator.group

import hammerlab.iterator.macros.IteratorOps
import spire.implicits._
import spire.math.Integral

/**
 * Run-length encode an input iterator, replacing contiguous runs of identical elements with pairs consisting of the
 * first element in the run and the number of elements observed.
 *
 * See RunLengthIteratorTest for examples.
 */
@IteratorOps
class RunLength[K](it: BufferedIterator[K]) {
  /**
   * Augment elements with an [[Int]] value representing the number of succeeding elements they are equivalent to (as
   * dictated by an implicit [[cats.Eq]] (if present; falls back to [[Ordering]]))
   */
  def runLengthEncode(implicit cmp: Cmp[K]): Iterator[(K, Int)] =
    runLengthPartial {
      case (cur, next)
        if (cmp(cur, next)) ⇒
        cur
    }

  /**
   * Fold succeeding runs of consecutive elements according to the provided partial-function.
   *
   * Anywhere `pf` is defined, the two adjacent input elements will be replaced with its value.
   */
  def runLengthPartial(pf: PartialFunction[(K, K), K]): Iterator[(K, Int)] =
    runLengthFunction(
      (l, r) ⇒
        if (pf.isDefinedAt((l, r)))
          Some(pf.apply((l, r)))
        else
          None
    )

  /**
   * Fold succeeding runs of consecutive elements according to the provided function.
   *
   * Anywhere it returns [[Some]], the two adjacent input elements will be replaced with the returned value.
   */
  def runLengthFunction(cmpFn: (K, K) ⇒ Option[K]): Iterator[(K, Int)] =
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
