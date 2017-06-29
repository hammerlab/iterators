package org.hammerlab.iterator

import org.hammerlab.iterator.GroupWithIterator._
import org.hammerlab.test.Suite

class GroupWithIteratorTest
  extends Suite {

  implicit def stringToInt(s: String): Int = augmentString(s).toInt

  test("mixed") {
    Iterator(2, 4, 6, 8, 10)
      .groupWith[String, Int](
        Iterator("1", "2", "3", "4", "5", "5", "7", "11")
      )
      .toList
      .map {
        case (t, us) ⇒
          t → us.toList
      } should be(
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
    Iterator[Int]()
      .groupWith[String, Int](
        Iterator("1", "2", "3", "4", "5", "5", "7", "11")
      )
      .toList
      .map {
        case (t, us) ⇒
          t → us.toList
      } should be(
      Nil
    )
  }
}
