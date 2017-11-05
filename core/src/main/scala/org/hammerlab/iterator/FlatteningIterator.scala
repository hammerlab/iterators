package org.hammerlab.iterator

import hammerlab.iterator._

/**
 * "Flatmap" over an [[Iterator]] of [[Iterator]]s, exposing the inner [[Iterator]] along the way (as its true type)
 *
 * @param it [[Iterator]] of [[Iterator]]s
 * @tparam V "value" type of resulting [[Iterator]]
 * @tparam T type of inner [[Iterator]]
 */
case class FlatteningIterator[V, T](it: BufferedIterator[T])(implicit ev: <:<[T, Iterator[V]])
  extends SimpleIterator[V] {
  private var _cur: Option[T] = _

  def _advanceIterator(): Option[T] = {
    it
      .nextOption
      .flatMap(
        nextIt ⇒
          if (nextIt.hasNext)
            Some(nextIt)
          else
            _advanceIterator()
      )
  }

  def hasNextIterator: Boolean = {
    if (_cur == null) {
      _cur = _advanceIterator()
    }
    _cur.nonEmpty
  }


  override protected def postNext(): Unit = {
    super.postNext()
    if (_cur.exists(!_.hasNext)) {
      _cur = null
    }
  }

  def cur: Option[T] = {
    if (hasNextIterator)
      _cur
    else
      None
  }

  def reset(): Unit = {
    _cur = null
    clear()
  }

  override protected def _advance: Option[V] =
    cur
      .map {
        _.next()
      }
}

case class FlattenableIterator[A](it: A) {
  // Synonym for "flatten", which the standard library is already squatting on
  def smush[V, T](implicit ev: <:<[A, Iterator[T]], ev2: <:<[T, Iterator[V]]): FlatteningIterator[V, T] =
    FlatteningIterator[V, T](it.buffered)
}

object FlatteningIterator {
  implicit def makeFlatmapIterator[A](it: A): FlattenableIterator[A] =
    FlattenableIterator(it)
}
