package org.hammerlab.iterator

/**
 * Given an [[Iterator[T]]], emit each element sandwiched between its preceding and succeeding elements.
 */
class Sliding3OptIterator[T](it: BufferedIterator[T]) {

  def sliding3: SimpleBufferedIterator[(Option[T], T, Option[T])] =
    new SimpleBufferedIterator[(Option[T], T, Option[T])] {
      private var lastOpt: Option[T] = None

      override protected def _advance: Option[(Option[T], T, Option[T])] = {
        if (!it.hasNext)
          return None

        val cur = it.next()

        val prevOpt = lastOpt
        lastOpt = Some(cur)

        val nextOpt =
          if (it.hasNext)
            Some(it.head)
          else
            None

        Some(
          (
            prevOpt,
            cur,
            nextOpt
          )
        )
      }
    }
}

object Sliding3OptIterator {
  implicit def make[T](it: Iterator[T]): Sliding3OptIterator[T] = new Sliding3OptIterator(it.buffered)
}
