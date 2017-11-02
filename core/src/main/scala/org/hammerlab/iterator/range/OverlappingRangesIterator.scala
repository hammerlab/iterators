package org.hammerlab.iterator.range

import org.hammerlab.iterator.SimpleBufferedIterator

import scala.collection.mutable

case class OverlappingRangesIterator[T: Ordering](it: BufferedIterator[Range[T]]) {

  type RangeT = (T, Option[T])

  val ≤ = implicitly[Ordering[T]].lteq _

  implicit val orderZippedRangeByEndOpt: Ordering[(Range[T], Int)] =
    Ordering
      .by[(Range[T], Int), Option[T]](_._1.endOpt)
      .reverse

  def joinOverlaps(other: Iterable[Range[T]]): Iterator[(Range[T], Vector[(Range[T], Int)])] =
    joinOverlaps(other.iterator.buffered)

  def joinOverlaps(other: BufferedIterator[Range[T]]): Iterator[(Range[T], Vector[(Range[T], Int)])] = {
    val queue = mutable.PriorityQueue[(Range[T], Int)]()

    val zippedOther =
      other
        .zipWithIndex
        .buffered

    new SimpleBufferedIterator[(Range[T], Vector[(Range[T], Int)])] {
      override protected def _advance: Option[(Range[T], Vector[(Range[T], Int)])] =
        it
          .nextOption
          .map {
            elem ⇒
              while (queue.headOption.exists(!_._1.∩(elem))) {
                queue.dequeue()
              }

              while (
                zippedOther
                  .headOption
                  .flatMap(_._1.endOpt)
                  .exists(≤(_, elem.start))
              ) {
                zippedOther.next
              }

              while (
                zippedOther
                  .headOption
                  .exists(_._1.∩(elem))
              ) {
                queue.enqueue(zippedOther.next)
              }

              elem →
                queue
                  .toVector
                  .sortBy(_._2)
          }
    }
  }
}

object OverlappingRangesIterator {
  implicit def makeOverlappingRangesIteratorFromIterable[T: Ordering](it: Iterable[Range[T]]): OverlappingRangesIterator[T] =
    OverlappingRangesIterator(it.iterator.buffered)

  implicit def makeOverlappingRangesIterator[T: Ordering](it: Iterator[Range[T]]): OverlappingRangesIterator[T] =
    OverlappingRangesIterator(it.buffered)
}
