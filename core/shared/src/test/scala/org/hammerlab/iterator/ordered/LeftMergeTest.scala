package org.hammerlab.iterator.ordered

import hammerlab.iterator._
import org.hammerlab

class LeftMergeTest
  extends hammerlab.Suite {

  implicit def stringToInt(s: String): Int = augmentString(s).toInt

  test("mixed") {
    ==(
      Iterator(2, 4, 6, 8, 10)
        .leftMerge[String, Int](
          Iterator("1", "2", "3", "4", "5", "5", "7", "11")
        )
        .toList
        .map {
          case (t, us) ⇒
            t → us.toList
        },
      Seq(
         2 → Seq("1", "2", "3"),
         4 → Seq("4", "5", "5"),
         6 → Seq("7"),
         8 → Seq(),
        10 → Seq("11")
      )
    )
  }

  test("left empty") {
    ==(
      Iterator[Int]()
        .leftMerge[String, Int](
          Iterator("1", "2", "3", "4", "5", "5", "7", "11")
        )
        .map {
          case (t, us) ⇒
            t → us.toList
        }
        .toList,
      Nil
    )
  }
}
