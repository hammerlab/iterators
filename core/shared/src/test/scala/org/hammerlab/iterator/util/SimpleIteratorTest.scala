package org.hammerlab.iterator.util

import hammerlab.iterator._
import org.hammerlab.Suite

class SimpleIteratorTest
  extends Suite {

  test("done") {
    var _done = false
    val it =
      new SimpleIterator[Int] {
        var elems = List(1, 2, 3)
        protected def _advance: Option[Int] = {
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

    ==(it.next, 1)
    ==(_done, false)
    ==(it.next, 2)
    ==(_done, false)
    ==(it.next, 3)
    ==(_done, false)
    ==(it.hasNext, false)
    ==(_done, true)

    intercept[NoSuchElementException] {
      it.head
    }

    intercept[NoSuchElementException] {
      it.next
    }
  }

  test("buffer") {
    ==(Iterator().buffer.toList, Nil)
    ==(Iterator(1).buffer.toList, Seq(1))
    ==(Iterator(1, 2).buffer.toList, Seq(1, 2))
    ==(Iterator(1, 2, 3).buffer.toList, Seq(1, 2, 3))
    ==(Iterator(1, 2, 3, 4).buffer.toList, Seq(1, 2, 3, 4))

    ==(Iterator().buffer.buffer.toList, Nil)
    ==(Iterator(1).buffer.buffer.toList, Seq(1))
    ==(Iterator(1, 2).buffer.buffer.toList, Seq(1, 2))
    ==(Iterator(1, 2, 3).buffer.buffer.toList, Seq(1, 2, 3))
    ==(Iterator(1, 2, 3, 4).buffer.buffer.toList, Seq(1, 2, 3, 4))
  }

  implicit class Bufferer[T](it: Iterator[T]) {
    def buffer: SimpleIterator[T] =
      it match {
        case sbi: SimpleIterator[T] ⇒ sbi
        case _ ⇒
          val buf = it.buffered
          new SimpleIterator[T] {
            protected def _advance: Option[T] =
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

  case class TestIterator(elems: Int*)
    extends SimpleIterator[Int] {
    val it = elems.iterator
    override protected def _advance: Option[Int] =
      it.nextOption
  }

  test("empty toString") {
    val it = TestIterator()
    ==(it.toString, "TestIterator")
    ==(it. hasNext,  false)
    ==(it.toString, "TestIterator (empty)")
  }

  test("non-empty toString") {
    val it = TestIterator(1, 2, 3)

    ==(it.toString, "TestIterator")
    ==(it.hasNext, true)
    ==(it.toString, "TestIterator (head: 1)")
    ==(it.next, 1)

    ==(it.toString, "TestIterator")
    ==(it.hasNext, true)
    ==(it.toString, "TestIterator (head: 2)")
    ==(it.next, 2)

    ==(it.toString, "TestIterator")
    ==(it.hasNext, true)
    ==(it.toString, "TestIterator (head: 3)")
    ==(it.next, 3)

    ==(it.toString, "TestIterator")
    ==(it.hasNext, false)
    ==(it.toString, "TestIterator (empty)")
  }
}
