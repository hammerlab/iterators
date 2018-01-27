package org.hammerlab.iterator.group

import hammerlab.iterator.macros.obj
import org.hammerlab.iterator.util.SimpleIterator

import scala.annotation.tailrec
import scala.collection.mutable

class SubsetOps[T](val s: Seq[T]) extends AnyVal {
  final def unorderedSubsetsWithReplacement(n: Int): Iterator[Seq[(T, Int)]] =
    if (s.isEmpty || n == 0)
      Iterator(Nil)
    else
      new SimpleIterator[Seq[(T, Int)]] {
        var cur: mutable.Stack[(Int, Int)] = _
        val ssize = s.size
        override protected def _advance: Option[Seq[(T, Int)]] = adv()

        @tailrec
        private def adv(numToPlace: Int = 0): Option[Seq[(T, Int)]] = {
          if (cur == null) {
            cur = mutable.Stack(0 → n)
            Some(Vector(s.head → n))
          } else {
            val (last, count) = cur.pop()
            if (last + 1 == ssize)
              if (cur.isEmpty)
                None
              else
                adv(count)
            else {
              if (count > 1) {
                cur.push(
                  (
                    last,
                    count - 1
                  )
                )
              }
              cur.push(
                (
                  last + 1,
                  numToPlace + 1
                )
              )
              Some(
                cur.foldLeft[List[(T, Int)]](Nil) {
                  case (l, (idx, count)) ⇒
                    (s(idx), count) :: l
                }
              )
            }
          }
        }
      }

  final def orderedSubsetsWithReplacement(n: Int): Iterator[Seq[T]] =
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
  implicit def make[T](s: Seq[T]): SubsetOps[T] = new SubsetOps(s)
}
