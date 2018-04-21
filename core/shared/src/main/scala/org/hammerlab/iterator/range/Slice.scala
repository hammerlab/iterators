package org.hammerlab.iterator.range

import hammerlab.iterator._
import hammerlab.iterator.macros.IteratorOps

import scala.reflect.ClassTag

@IteratorOps
class Slice[T](it: Iterator[T]) {
  def sliceOpt(start: Option[Int], end: Option[Int]): Iterator[T] = {
    start.foreach { it.dropEager }
    end.fold { it } { end ⇒ it.take { end - start.getOrElse(0) } }
  }
  def sliceOpt(start: Int, end: Int): Iterator[T] = sliceOpt(Some(start), Some(end))
  def sliceOpt(start: Option[Int], end: Int): Iterator[T] = sliceOpt(start, Some(end))
  def sliceOpt(start: Int, end: Option[Int] = None): Iterator[T] = sliceOpt(Some(start), end)

  def take(start: Option[Int])(implicit ct: ClassTag[T]): Iterator[T] =
    start.fold { it } { it.take }

  def drop(start: Option[Int]): Iterator[T] =
    start.fold { it } { it.dropEager }
}
