package org.hammerlab.iterator

/**
 * [[Iterator]]s constructed via [[Iterator.apply]] or [[scala.collection.IndexedSeqLike.iterator]] are tricky about
 * what state they are left in after various operations are applied, cf. https://github.com/scala/bug/issues/9274.
 *
 * This class allows easy creation of vanilla [[Iterator]]s with no funny-business, for testing extended iterator
 * functionality like [[BufferedTakeWhileIteratorTest]], which passes erroneously with naively-created [[Iterator]]s.
 */
case class TestIterator[T](elems: T*)
  extends Iterator[T] {
  var idx = 0
  override def hasNext: Boolean = idx < elems.size

  override def next(): T = {
    val elem = elems(idx)
    idx += 1
    elem
  }
}
