package org.hammerlab.iterator

/**
 * Given an iterator and a predicate function, emit iterators containing maximal runs of sequential elements that all
 * satisfy the predicate, or individual elements that do not.
 *
 * For example, given an [[Iterator]] containing [[Int]]s [1, 2, 4, 3, 5, 6, 2, 8] and predicate function `_ % 2 == 0`,
 * [[GroupRunsIterator]] would emit [[Iterator]]s containing [1], [2, 4], [3], [5], [6, 2, 8].
 *
 * See GroupRunsIteratorTest for more examples.
 */
object GroupRunsIterator {
  implicit def groupRunsFromIterable[T](it: Iterable[T]): GroupRunsIterator[T] = new GroupRunsIterator(it.iterator.buffered)
  implicit def groupRunsFromIterator[T](it: Iterator[T]): GroupRunsIterator[T] = new GroupRunsIterator(it.buffered)

  implicit class GroupRunsIterator[T](val it: BufferedIterator[T])
    extends AnyVal {

    def groupBy(pred: T ⇒ Boolean): Iterator[Iterator[T]] =
      new Iterator[Iterator[T]] {

        override def hasNext: Boolean = it.hasNext

        override def next(): Iterator[T] = {
          if (!pred(it.head))
            Iterator(it.next())
          else
            new SimpleBufferedIterator[T] {
              override protected def _advance: Option[T] =
                if (it.hasNext && pred(it.head))
                  Some(it.next())
                else
                  None
            }
        }
      }

    def groupRuns(implicit ord: Ordering[T]): Iterator[Iterator[T]] = {
      new Iterator[Iterator[T]] {
        var prevOpt: Option[T] = None
        override def hasNext: Boolean = it.hasNext
        override def next(): Iterator[T] = {
          val n = it.next()
          Iterator(n) ++
            new SimpleBufferedIterator[T] {
              override protected def _advance: Option[T] =
                if (it.hasNext && ord.compare(n, it.head) == 0)
                  Some(it.next)
                else
                  None
            }
        }
      }
    }
  }
}
