package org.hammerlab.iterator

import org.hammerlab.test.Suite

import SimpleBufferedIterator._

class SimpleBufferedIteratorTest
  extends Suite {

  test("done") {
    var _done = false
    val it =
      new SimpleBufferedIterator[Int] {
        var elems = List(1, 2, 3)
        override protected def _advance: Option[Int] = {
          elems.headOption match {
            case Some(elem) ⇒
              elems = elems.tail
              Some(elem)
            case None ⇒
              None
          }
        }

        override def done(): Unit = {
          _done = true
        }
      }

    it.next should be(1)
    _done should be(false)
    it.next should be(2)
    _done should be(false)
    it.next should be(3)
    _done should be(false)
    it.hasNext should be(false)
    _done should be(true)
  }

  test("buffer") {
    Iterator().buffer.toList should be(Nil)
    Iterator(1).buffer.toList should be(Seq(1))
    Iterator(1, 2).buffer.toList should be(Seq(1, 2))
    Iterator(1, 2, 3).buffer.toList should be(Seq(1, 2, 3))
    Iterator(1, 2, 3, 4).buffer.toList should be(Seq(1, 2, 3, 4))

    Iterator().buffer.buffer.toList should be(Nil)
    Iterator(1).buffer.buffer.toList should be(Seq(1))
    Iterator(1, 2).buffer.buffer.toList should be(Seq(1, 2))
    Iterator(1, 2, 3).buffer.buffer.toList should be(Seq(1, 2, 3))
    Iterator(1, 2, 3, 4).buffer.buffer.toList should be(Seq(1, 2, 3, 4))
  }
}
