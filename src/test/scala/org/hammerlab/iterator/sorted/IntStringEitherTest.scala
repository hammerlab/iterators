package org.hammerlab.iterator.sorted

import scala.collection.immutable.StringOps

trait IntStringEitherTest
  extends EitherZip {

  /**
   * Workaround [[strlen]] making [[augmentString]] implicit (for accessing
   * [[scala.collection.immutable.StringLike.*]]) ambiguous.
   */
  implicit class StringMult(val s: String) {
    def ｘ(n: Int): String = (s: StringOps) * n
  }

  case class WrappedInt(n: Int)
  implicit val wrapInt: Int ⇒ WrappedInt = WrappedInt
  val unwrapWrappedInt: WrappedInt ⇒ Int = _.n

  val wrappedInts =
    Seq[WrappedInt](
       1,
       2,
       4,
       7,
      10,
      15
    )

  val strings =
    Seq(
      "",
      "a",
      "a",
      "b" ｘ  2,
      "c" ｘ  3,
      "e" ｘ  5,
      "f" ｘ  6,
      "k" ｘ 11,
      "n" ｘ 14
    )

  def strlen(s: String): Int = s.length
}
