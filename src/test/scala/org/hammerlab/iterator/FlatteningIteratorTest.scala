package org.hammerlab.iterator

import org.hammerlab.test.Suite
import FlatteningIterator._

class FlatteningIteratorTest
  extends Suite {

  test("empty") {
    Iterator().smush[Nothing, Iterator[Nothing]].toList should be(Nil)
  }

  test("one empty") {
    Iterator(Iterator()).smush.toList should be(Nil)
  }

  test("two empties") {
    Iterator(Iterator(), Iterator()).smush.toList should be(Nil)
  }

  test("one empty one elem") {
    makeFlatmapIterator(Iterator(Iterator(), Iterator(1))).smush.toList should be(List(1))
  }

  test("one empty two elems") {
    Iterator(Iterator(), Iterator(1, 2)).smush.toList should be(List(1, 2))
  }

  test("one elem one empty") {
    Iterator(Iterator(1), Iterator()).smush.toList should be(List(1))
  }

  test("two elems one empty") {
    Iterator(Iterator(1, 2), Iterator()).smush.toList should be(List(1, 2))
  }

  test("two ones") {
    Iterator(Iterator(1), Iterator(2)).smush.toList should be(List(1, 2))
  }

  test("many elems") {
    Iterator(
      Iterator(1, 2, 3),
      Iterator(),
      Iterator(4, 5),
      Iterator(6),
      Iterator(7, 8, 9),
      Iterator(),
      Iterator(10)
    )
    .smush
    .toList should be(
      1 to 10
    )
  }

  /**
   * Dummy [[Iterator]] implementation used for validating [[FlatteningIterator.cur]] book-keeping.
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
      .smush

    it.cur should be(Some(Run(1, 5)))
    it.next should be(1)
    it.cur should be(Some(Run(1, 5)))
    it.next should be(2)
    it.cur should be(Some(Run(1, 5)))
    it.next should be(3)
    it.cur should be(Some(Run(1, 5)))
    it.next should be(4)

    it.cur should be(Some(Run(10, 15)))
    it.next should be(10)
    it.cur should be(Some(Run(10, 15)))
    it.next should be(11)
    it.cur should be(Some(Run(10, 15)))
    it.next should be(12)
    it.cur should be(Some(Run(10, 15)))
    it.next should be(13)
    it.cur should be(Some(Run(10, 15)))
    it.next should be(14)

    it.cur should be(Some(Run(20, 25)))
    it.next should be(20)
    it.cur should be(Some(Run(20, 25)))
    it.next should be(21)
    it.cur should be(Some(Run(20, 25)))
    it.next should be(22)
    it.cur should be(Some(Run(20, 25)))
    it.next should be(23)
    it.cur should be(Some(Run(20, 25)))
    it.next should be(24)

    it.cur should be(None)
    it.hasNext should be(false)
  }
}
