package org.hammerlab.iterator

import hammerlab.iterator._
import hammerlab.iterator.macros.IteratorWrapper

@IteratorWrapper
class Slice[T](it: Iterator[T]) {
  def sliceOpt(start: Option[Int], length: Option[Int]): Iterator[T] = {
    start.foreach(it.dropEager)
    length.map(it.take).getOrElse(it)
  }
  def sliceOpt(start: Int, length: Int): Iterator[T] = sliceOpt(Some(start), Some(length))
  def sliceOpt(start: Option[Int], length: Int): Iterator[T] = sliceOpt(start, Some(length))
  def sliceOpt(start: Int, length: Option[Int] = None): Iterator[T] = sliceOpt(Some(start), length)
}