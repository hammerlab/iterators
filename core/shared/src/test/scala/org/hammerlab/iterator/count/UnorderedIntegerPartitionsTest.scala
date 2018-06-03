package org.hammerlab.iterator.count

import hammerlab.iterator.count._
import org.hammerlab.Suite

class UnorderedIntegerPartitionsTest
  extends Suite {

  implicit def seqOrd[T](implicit ord: Ordering[T]): Ordering[List[T]] =
    new Ordering[List[T]] {
      override def compare(x: List[T], y: List[T]): Int =
        (x.isEmpty, y.isEmpty) match {
          case ( true,  true) ⇒  0
          case (false,  true) ⇒  1
          case ( true, false) ⇒ -1
          case (false, false) ⇒
            ord.compare(x.head, y.head) match {
              case 0 ⇒ compare(x.tail, y.tail)
              case x ⇒ x
            }
        }
    }

  def check(n: Int): Unit =
    withClue(s"n: $n: ") {
      val actual = n.unorderedPartitions.toList
      actual.foreach(a ⇒ ==(a.sum, n))    // all add up to `n`
      ==(actual.toSet.size, actual.size)  // all distinct
      ==(actual.sorted.reverse, actual)   // descending order
    }

  test("simple") {

    def chk(n: Int, expected: String): Unit =
      ==(
        n.unorderedPartitions.map(_.mkString(",")).mkString("\n"),
        expected.stripMargin
      )

    ==(0.unorderedPartitions.toSeq, Seq(Nil))
    ==(1.unorderedPartitions.toSeq, Seq(Seq(1)))
    chk(
      2,
      """2
        |1,1"""
    )
    chk(
      3,
      """3
        |2,1
        |1,1,1"""
    )
    chk(
      4,
      """4
        |3,1
        |2,2
        |2,1,1
        |1,1,1,1"""
    )
    chk(
      5,
      """5
        |4,1
        |3,2
        |3,1,1
        |2,2,1
        |2,1,1,1
        |1,1,1,1,1"""
    )
    chk(
      6,
      """6
        |5,1
        |4,2
        |4,1,1
        |3,3
        |3,2,1
        |3,1,1,1
        |2,2,2
        |2,2,1,1
        |2,1,1,1,1
        |1,1,1,1,1,1"""
    )

    0 to 10 foreach check
  }
}
