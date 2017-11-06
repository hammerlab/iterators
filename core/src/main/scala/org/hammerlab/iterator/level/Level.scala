package org.hammerlab.iterator.level

/**
 * Syntax for creating a [[LevelingIterator]]
 */
class LevelOps[A](val it: A) extends AnyVal {
  def level[V, T](implicit ev: <:<[A, Iterator[T]], ev2: <:<[T, Iterator[V]]): LevelingIterator[V, T] =
    LevelingIterator[V, T](it.buffered)
}

trait Level {
  implicit def makeLevel[A](it: A): LevelOps[A] = new LevelOps(it)
}
