package org.hammerlab.iterator.util

import hammerlab.iterator._

/**
 * Interface for implementing [[BufferedIterator]]s following a common pattern: the `hasNext` implementation must
 * actually compute the next element (or return a sentinel that implies that one doesn't exist), and should therefore
 * cache that element to serve `head` and `next()` calls.
 *
 * This interface allows subclasses to simply implement an `_advance` method that returns an [[Option]] containing the
 * next element, if one exists, and [[None]] otherwise, and it takes care of the rest of the boilerplate.
 *
 * It also exposes protected `clear` and `postNext` methods for [[None]]ing the internal state and responding to
 * `next()` having been called, respectively.
 */
trait SimpleIterator[+T]
  extends BufferedIterator[T] {

  /**
   * Compute and return the next element
   */
  protected def _advance: Option[T]

  /**
   * null when not initialized, [[Some]] when there is a next value cached, and [[None]] when there are no more values.
   */
  private[this] var _next: Option[T] = _

  def clear(): Unit = {
    _next = null
  }

  /**
   * Compute the next element, if it hasn't already been computed; the result is stored in [[_next]].
   */
  override final def hasNext: Boolean = {
    if (_next == null) {
      _next = _advance
    }
    _next.nonEmpty || { done(); false }
  }

  override final def head: T = {
    if (!hasNext)
      throw new NoSuchElementException

    _next.get
  }

  /** Hook for subclasses to update books after each element is consumed. */
  protected def postNext(): Unit = {}

  override final def next(): T = {
    val r = head
    clear()
    postNext()
    r
  }

  override def toString(): String = {
    _next match {
      case null ⇒ s"${getClass.getSimpleName}"
      case Some(next) ⇒ s"${getClass.getSimpleName} (head: $head)"
      case None ⇒ s"${getClass.getSimpleName} (empty)"
    }
  }

  /** Called exactly once, when the iterator is empty */
  protected def done(): Unit = {}
}
