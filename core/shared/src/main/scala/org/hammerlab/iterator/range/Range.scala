package org.hammerlab.iterator.range

case class Range[T](start: T, endOpt: Option[T]) {
  def ∩(right: Range[T])(implicit ord: Ordering[T]): Boolean = {
    val ≤ = ord.lteq _
    val Range(rightStart, rightEndOpt) = right
    if (≤(start, rightStart))
      !endOpt.exists(≤(_, rightStart))
    else
      !rightEndOpt.exists(≤(_, start))
  }

  override def toString: String =
    s"[$start,${endOpt.getOrElse("∞")})"
}

object Range {
  def apply[T](start: T, end: T): Range[T] = Range(start, Some(end))
  def apply[T](start: T): Range[T] = Range(start, None)

  implicit def fromPair[T](t: (T, T)): Range[T] = Range(t._1, t._2)

  implicit def endOptOrdering[T](implicit ord: Ordering[T]): Ordering[Option[T]] =
    new Ordering[Option[T]] {
      override def compare(x: Option[T], y: Option[T]): Int =
        (x, y) match {
          case (None, None) ⇒ 0
          case (None, _) ⇒ -1
          case (_, None) ⇒ 1
          case (Some(x), Some(y)) ⇒ ord.compare(x, y)
        }
    }

  implicit def orderByStartThenEnd[T: Ordering]: Ordering[Range[T]] = {

    implicit val tupleOrdering =
      Ordering.Tuple2[T, Option[T]]

    Ordering.by[Range[T], (T, Option[T])] {
      case Range(start, endOpt) ⇒
        start → endOpt
    }
  }
}
