package org.hammerlab.stats

import cats.Show
import cats.Show.show
import cats.implicits._
import spire.math.Integral

/**
 * Convenience class wrapping a sequence of key-number pairs, used in run-length-encoding in [[Stats]].
 */
case class Runs[K, V: Integral](elems: Seq[(K, V)])

object Runs {
  implicit def runsToSeq[K, V: Integral](runs: Runs[K, V]): Seq[(K, V)] = runs.elems
  implicit def seqToRuns[K, V: Integral](elems: Seq[(K, V)]): Runs[K, V] = Runs(elems)

  implicit def makeShow[K, V: Integral](implicit elemShow: Show[K], countShow: Show[V]): Show[Runs[K, V]] =
    show {
      case Runs(elems) ⇒
        elems
          .map {
            case (elem, count) ⇒
              if (count == 1)
                elem.show
              else
                s"${elem.show}×${count.show}"
          }
          .mkString(", ")
    }
}
