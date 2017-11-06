package org.hammerlab.iterator.util

import hammerlab.iterator._
import org.hammerlab.test.Suite

class SimpleIteratorTest
  extends Suite {

  test("done") {
    var _done = false
    val it =
      new SimpleIterator[Int] {
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

    intercept[NoSuchElementException] {
      it.head
    }

    intercept[NoSuchElementException] {
      it.next
    }
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

  implicit class Bufferer[T](it: Iterator[T]) {
    def buffer: SimpleIterator[T] = {
      it match {
        case sbi: SimpleIterator[T] ⇒ sbi
        case _ ⇒
          val buf = it.buffered
          new SimpleIterator[T] {
            override protected def _advance: Option[T] = {
              buf
                .headOption
                .map {
                  elem ⇒
                    buf.next()
                    elem
                }
            }
          }
      }
    }
  }

  case class TestIterator(elems: Int*)
    extends SimpleIterator[Int] {
    val it = elems.iterator
    override protected def _advance: Option[Int] =
      it.nextOption
  }

  test("empty toString") {
    val it = TestIterator()
    it.toString should be("TestIterator")
    it.hasNext should be(false)
    it.toString should be("TestIterator (empty)")
  }

  test("non-empty toString") {
    val it = TestIterator(1, 2, 3)

    it.toString should be("TestIterator")
    it.hasNext should be(true)
    it.toString should be("TestIterator (head: 1)")
    it.next should be(1)

    it.toString should be("TestIterator")
    it.hasNext should be(true)
    it.toString should be("TestIterator (head: 2)")
    it.next should be(2)

    it.toString should be("TestIterator")
    it.hasNext should be(true)
    it.toString should be("TestIterator (head: 3)")
    it.next should be(3)

    it.toString should be("TestIterator")
    it.hasNext should be(false)
    it.toString should be("TestIterator (empty)")
  }
}
