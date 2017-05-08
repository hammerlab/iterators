package org.hammerlab.iterator

case class HeadOptionIterator[T](it: BufferedIterator[T]) {
  def headOption: Option[T] =
    if (it.hasNext)
      Some(it.head)
    else
      None

  def nextOption: Option[T] =
    if (it.hasNext)
      Some(it.next)
    else
      None
}

object HeadOptionIterator {
  implicit def makeHeadOptionIterator[T](it: BufferedIterator[T]): HeadOptionIterator[T] = HeadOptionIterator(it)
}
