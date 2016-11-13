package org.hammerlab.math

import java.io.ByteArrayOutputStream

import com.esotericsoftware.kryo.io.{Input, Output}
import org.scalatest.{FunSuite, Matchers}

class VarNumTest extends FunSuite with Matchers {

  // [0,8]
  testBytes(0, List(0))
  testBytes(1, List(1))
  testBytes(2, List(2))
  testBytes(3, List(3))
  testBytes(4, List(4))
  testBytes(5, List(5))
  testBytes(6, List(6))
  testBytes(7, List(7))
  testBytes(8, List(8))

  // Jump from 1 byte to 2 bytes at 2^6.
  testBytes(0x3b, List(0x3b))
  testBytes(0x3f, List(0x3f))
  testBytes(0x40, List(0x80, 0x01))
  testBytes(0x41, List(0x81, 0x01))
  testBytes(0x42, List(0x82, 0x01))
  testBytes(0x43, List(0x83, 0x01))
  testBytes(0x44, List(0x84, 0x01))
  testBytes(0x45, List(0x85, 0x01))

  // Jump from 2 bytes to 3 bytes at 2^13.
  testBytes(0x1fff, List(0xbf, 0x7f))
  testBytes(0x2000, List(0x80, 0x80, 0x01))
  testBytes(0x2001, List(0x81, 0x80, 0x01))
  testBytes(0x2002, List(0x82, 0x80, 0x01))
  testBytes(0x2003, List(0x83, 0x80, 0x01))
  testBytes(0x2004, List(0x84, 0x80, 0x01))

  // 3 bytes to 4 bytes at 2^20.
  testBytes( 0xfffff, List(0xbf, 0xff, 0x7f))
  testBytes(0x100000, List(0x80, 0x80, 0x80, 0x01))
  testBytes(0x100001, List(0x81, 0x80, 0x80, 0x01))
  testBytes(0x100002, List(0x82, 0x80, 0x80, 0x01))
  testBytes(0x100003, List(0x83, 0x80, 0x80, 0x01))
  testBytes(0x100004, List(0x84, 0x80, 0x80, 0x01))

  // 4 bytes to 5 bytes at 2^27.
  testBytes(0x7ffffff, List(0xbf, 0xff, 0xff, 0x7f))
  testBytes(0x8000000, List(0x80, 0x80, 0x80, 0x80, 0x01))
  testBytes(0x8000001, List(0x81, 0x80, 0x80, 0x80, 0x01))
  testBytes(0x8000002, List(0x82, 0x80, 0x80, 0x80, 0x01))
  testBytes(0x8000003, List(0x83, 0x80, 0x80, 0x80, 0x01))
  testBytes(0x8000004, List(0x84, 0x80, 0x80, 0x80, 0x01))

  // 5 bytes to 6 bytes at 2^34.
  testBytes(0x3ffffffffL, List(0xbf, 0xff, 0xff, 0xff, 0x7f))
  testBytes(0x400000000L, List(0x80, 0x80, 0x80, 0x80, 0x80, 0x01))
  testBytes(0x400000001L, List(0x81, 0x80, 0x80, 0x80, 0x80, 0x01))
  testBytes(0x400000002L, List(0x82, 0x80, 0x80, 0x80, 0x80, 0x01))
  testBytes(0x400000003L, List(0x83, 0x80, 0x80, 0x80, 0x80, 0x01))
  testBytes(0x400000004L, List(0x84, 0x80, 0x80, 0x80, 0x80, 0x01))

  // Largest Long.
  testBytes(0x7fffffffffffffffL, List(0xbf, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff))

  // Every 4th Fibonacci number from 21 to 2^63.
  testBytes(                 21L, List(0x15))
  testBytes(                144L, List(0x90, 0x02))
  testBytes(                987L, List(0x9b, 0x0f))
  testBytes(               6765L, List(0xad, 0x69))
  testBytes(              46368L, List(0xa0, 0xd4, 0x05))
  testBytes(             317811L, List(0xb3, 0xe5, 0x26))
  testBytes(            2178309L, List(0x85, 0xf4, 0x89, 0x02))
  testBytes(           14930352L, List(0xb0, 0xc6, 0x9e, 0x0e))
  testBytes(          102334155L, List(0x8b, 0xfb, 0xcb, 0x61))
  testBytes(          701408733L, List(0x9d, 0x97, 0xf5, 0x9c, 0x05))
  testBytes(         4807526976L, List(0x80, 0xa9, 0xe8, 0xe8, 0x23))
  testBytes(        32951280099L, List(0xa3, 0x87, 0xe5, 0xc0, 0xf5, 0x01))
  testBytes(       225851433717L, List(0xb5, 0x8b, 0xdb, 0xdc, 0x92, 0x0d))
  testBytes(      1548008755920L, List(0x90, 0xcb, 0x98, 0xc8, 0x8d, 0x5a))
  testBytes(     10610209857723L, List(0xbb, 0x82, 0xd1, 0x9c, 0xcc, 0xe9, 0x04))
  testBytes(     72723460248141L, List(0x8d, 0xc9, 0x9e, 0x80, 0x88, 0x89, 0x21))
  testBytes(    498454011879264L, List(0xa0, 0xfd, 0x84, 0xe5, 0xeb, 0xd5, 0xe2, 0x01))
  testBytes(   3416454622906707L, List(0x93, 0xa5, 0x84, 0xc3, 0xea, 0xcf, 0x91, 0x0c))
  testBytes(  23416728348467685L, List(0xa5, 0x87, 0x99, 0xf0, 0xfd, 0xd8, 0x98, 0x53))
  testBytes( 160500643816367088L, List(0xb0, 0x8f, 0xab, 0xce, 0x86, 0x9f, 0x9b, 0xba, 0x04))
  testBytes(1100087778366101931L, List(0xab, 0xe6, 0x94, 0xb4, 0xb0, 0x80, 0xa6, 0xc4, 0x1e))
  testBytes(7540113804746346429L, List(0xbd, 0xbe, 0xe6, 0x9e, 0xcc, 0xe3, 0xee, 0xa3, 0xd1))

  /**
   * Generate test cases that verify that:
   *
   *   - `l` is written to bytes matching `expected` bytes,
   *   - those `expected` bytes are read back in to a [[Long]] equal to `l`,
   *   - and then do the same for the additive inverse of `l` with a list of `expected` bytes where the sign bit (0x40
   *     in the first byte) is flipped.
   */
  def testBytes(l: Long, expected: List[Int]): Unit = {

    test(s"0x${l.toHexString} ($l)") {
      run(l, expected)
    }

    if (l > 0) {
      val negExpected = {
        val head = expected.head
        if ((head & 0x40) > 0) {
          throw new IllegalArgumentException(s"Expected bytes [${expected.mkString(",")}] already have sign bit set negative")
        }
        (head | 0x40) :: expected.tail
      }

      test(s"-0x${l.toHexString} (-$l)") {
        run(-l, negExpected)
      }
    }
  }

  def run(l: Long, expected: List[Int]): Unit = {
    val baos = new ByteArrayOutputStream()

    val op = new Output(baos)
    VarNum.write(op, l)
    op.close()

    val bytes = baos.toByteArray
    bytes should be(expected.toArray.map(_.toByte))

    val ip = new Input(bytes)
    VarNum.read(ip) should be(l)
    ip.close()
  }
}
