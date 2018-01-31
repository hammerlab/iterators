package org.hammerlab.iterator.count

import hammerlab.iterator.macros.obj
import math.min

class UnorderedIntegerPartitionsOps(val n: Int)
  extends AnyVal {
  def unorderedPartitions: Iterator[List[Int]] =
    if (n < 0)
      throw new IllegalArgumentException(s"$n")
    else
      unorderedPartitionsImpl(n, n)

  private def unorderedPartitionsImpl(n: Int, max: Int): Iterator[List[Int]] =
    if (n == 0)
      Iterator(Nil)
    else
      for {
        next ← (min(n, max) to 1 by -1).iterator
        rest ← unorderedPartitionsImpl(n - next, next)
      } yield
        next :: rest
}

@obj trait UnorderedIntegerPartitions {
  implicit def makeUnorderedIntegerPartitionsOps(n: Int): UnorderedIntegerPartitionsOps = new UnorderedIntegerPartitionsOps(n)
}
