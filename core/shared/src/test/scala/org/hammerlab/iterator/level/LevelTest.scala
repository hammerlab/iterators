package org.hammerlab.iterator.level

import hammerlab.iterator.level._
import org.hammerlab.Suite

class LevelTest
  extends Suite {

  test("empty") {
    ==(Iterator().level[Nothing, Iterator[Nothing]].toList, Nil)
  }

  test("one empty") {
    ==(Iterator(Iterator()).level.toList, Nil)
  }

  test("two empties") {
    ==(Iterator(Iterator(), Iterator()).level.toList, Nil)
  }

  test("one empty one elem") {
    ==(Iterator(Iterator(), Iterator(1)).level.toList, List(1))
  }

  test("one empty two elems") {
    ==(Iterator(Iterator(), Iterator(1, 2)).level.toList, List(1, 2))
  }

  test("one elem one empty") {
    ==(Iterator(Iterator(1), Iterator()).level.toList, List(1))
  }

  test("two elems one empty") {
    ==(Iterator(Iterator(1, 2), Iterator()).level.toList, List(1, 2))
  }

  test("two ones") {
    ==(Iterator(Iterator(1), Iterator(2)).level.toList, List(1, 2))
  }

  test("many elems") {
    ==(
      Iterator(
        Iterator(1, 2, 3),
        Iterator(),
        Iterator(4, 5),
        Iterator(6),
        Iterator(7, 8, 9),
        Iterator(),
        Iterator(10)
      )
      .level
      .toSeq,
      1 to 10
    )
  }

  /**
   * Dummy [[Iterator]] implementation used for validating [[LevelingIterator.cur]] book-keeping.
   */
  case class Run(start: Int, end: Int)
    extends Iterator[Int] {
    var idx = start
    override def hasNext: Boolean = idx < end

    override def next(): Int = {
      val ret = idx
      idx += 1
      ret
    }
  }

  test("runs") {
    val it =
      Iterator(
        Run( 1,  5),
        Run(10, 15),
        Run(20, 25)
      )
      .level

    ==(it.cur, Some(Run(1, 5)))
    ==(it.next, 1)
    ==(it.cur, Some(Run(1, 5)))
    ==(it.next, 2)
    ==(it.cur, Some(Run(1, 5)))
    ==(it.next, 3)
    ==(it.cur, Some(Run(1, 5)))
    ==(it.next, 4)

    ==(it.cur, Some(Run(10, 15)))
    ==(it.next, 10)
    ==(it.cur, Some(Run(10, 15)))
    ==(it.next, 11)
    ==(it.cur, Some(Run(10, 15)))
    ==(it.next, 12)
    ==(it.cur, Some(Run(10, 15)))
    ==(it.next, 13)
    ==(it.cur, Some(Run(10, 15)))
    ==(it.next, 14)

    ==(it.cur, Some(Run(20, 25)))
    ==(it.next, 20)
    ==(it.cur, Some(Run(20, 25)))
    ==(it.next, 21)
    ==(it.cur, Some(Run(20, 25)))
    ==(it.next, 22)
    ==(it.cur, Some(Run(20, 25)))
    ==(it.next, 23)
    ==(it.cur, Some(Run(20, 25)))
    ==(it.next, 24)

    ==(it.cur, None)
    ==(it.hasNext, false)
  }
}
