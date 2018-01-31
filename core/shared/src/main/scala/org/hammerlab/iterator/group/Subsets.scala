package org.hammerlab.iterator.group

import hammerlab.iterator.macros.obj
import org.hammerlab.iterator.group.Subsets.makeSubsetOps

class SubsetOps[T](val s: Seq[T]) extends AnyVal {
  final def unorderedSubsetsWithReplacement(k: Int): Iterator[List[(T, Int)]] =
    if (k == 0)
      Iterator(Nil)
    else
      s match {
        case Seq(e) ⇒ Iterator(List(e → k))
        case Seq(head, tail @ _*) ⇒
          (
            for {
              n ← (k to 1 by -1).iterator
              subset ← tail.unorderedSubsetsWithReplacement(k - n)
            } yield
              (head, n) :: subset
          ) ++
          tail.unorderedSubsetsWithReplacement(k)
        case _ ⇒ Iterator()
      }

  /**
   * Like [[scala.collection.SeqLike.combinations]], but doesn't dedupe elements; returned iterator has size nCk, where
   * `n` is the size of the input sequence ([[s]]) and `k` is the parameter to this function
   */
  def unorderedSubsets(k: Int): Iterator[List[T]] =
    if (k == 0)
      Iterator(Nil)
    else if (k > s.size)
      Iterator()
    else {
      val (head, tail) = (s.head, s.tail)
      tail
        .unorderedSubsets(k - 1)
        .map {
          subset ⇒ head :: subset
        } ++
      tail.unorderedSubsets(k)
    }

  final def orderedSubsetsWithReplacement(n: Int): Iterator[List[T]] =
    if (s.isEmpty || n == 0)
      Iterator(Nil)
    else
      for {
        e ← s.iterator
        subset ← orderedSubsetsWithReplacement(n - 1)
      } yield
        e :: subset.toList
}

@obj trait Subsets {
  implicit def makeSubsetOps[T](s: Seq[T]): SubsetOps[T] = new SubsetOps(s)
}
