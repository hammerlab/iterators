package org.hammerlab.stats

import cats.Show
import cats.instances.all.{ catsStdShowForInt, catsStdShowForLong }
import cats.syntax.all._
import org.hammerlab.test.Suite
import spire.math.Numeric

import scala.util.Random.{ nextInt, setSeed, shuffle }

/**
 * Test constructing [[Stats]] instances.
 */
class ShowTest extends Suite {

  setSeed(123L)

  def check[K : Numeric : Ordering : Show](input: Seq[K], lines: String*): Unit =
    Stats(input).show should be(
      lines.mkString("\n")
    )

  def check[K : Numeric : Ordering : Show](input: Seq[K], numToSample: Int, lines: String*): Unit =
    Stats(
      input,
      numToSample
    )
    .show should be(
      lines.mkString("\n")
    )

  def check[K : Numeric : Ordering : Show](input: Seq[K],
                                           numToSample: Int,
                                           onlySampleSorted: Boolean,
                                           lines: String*): Unit =
    Stats(
      input,
      numToSample,
      onlySampleSorted
    )
    .show should be(
      lines.mkString("\n")
    )

  test("empty") {
    check[Int](
      Nil,
      "(empty)"
    )
  }

  test("0 to 0") {
    check(
      0 to 0,
      "num:	1,	mean:	0,	stddev:	0,	mad:	0",
      "elems:	0"
    )
  }

  test("0 to 1") {
    check(
      0 to 1,
      "num:	2,	mean:	0.5,	stddev:	0.5,	mad:	0.5",
      "elems:	0, 1"
    )
  }

  test("1 to 0") {
    check(
      1 to 0 by -1,
      "num:	2,	mean:	0.5,	stddev:	0.5,	mad:	0.5",
      "elems:	1, 0",
      "sorted:	0, 1"
    )
  }

  test("0 to 2") {
    check(
      0 to 2,
      "num:	3,	mean:	1,	stddev:	0.8,	mad:	1",
      "elems:	0, 1, 2",
      "50:	1"
    )
  }

  test("2 to 0") {
    check(
      2 to 0 by -1,
      "num:	3,	mean:	1,	stddev:	0.8,	mad:	1",
      "elems:	2, 1, 0",
      "sorted:	0, 1, 2",
      "50:	1"
    )
  }

  test("0 to 3") {
    check(
      0 to 3,
      "num:	4,	mean:	1.5,	stddev:	1.1,	mad:	1",
      "elems:	0, 1, 2, 3",
      "50:	1.5"
    )
  }

  test("3 to 0") {
    check(
      3 to 0 by -1,
      "num:	4,	mean:	1.5,	stddev:	1.1,	mad:	1",
      "elems:	3, 2, 1, 0",
      "sorted:	0, 1, 2, 3",
      "50:	1.5"
    )
  }

  test("1 to 3") {
    check(
      1 to 3,
      "num:	3,	mean:	2,	stddev:	0.8,	mad:	1",
      "elems:	1, 2, 3",
      "25:	1",
      "50:	2",
      "75:	3"
    )
  }

  test("3 to 1") {
    check(
      3 to 1 by -1,
      "num:	3,	mean:	2,	stddev:	0.8,	mad:	1",
      "elems:	3, 2, 1",
      "sorted:	1, 2, 3",
      "25:	1",
      "50:	2",
      "75:	3"
    )
  }

  test("1 to 9") {
    check(
      1 to 9,
      "num:	9,	mean:	5,	stddev:	2.6,	mad:	2",
      "elems:	1, 2, 3, 4, 5, 6, 7, 8, 9",
      "10:	1",
      "25:	2.5",
      "50:	5",
      "75:	7.5",
      "90:	9"
    )
  }

  test("9 to 1") {
    check(
      9 to 1 by -1,
      "num:	9,	mean:	5,	stddev:	2.6,	mad:	2",
      "elems:	9, 8, 7, 6, 5, 4, 3, 2, 1",
      "sorted:	1, 2, 3, 4, 5, 6, 7, 8, 9",
      "10:	1",
      "25:	2.5",
      "50:	5",
      "75:	7.5",
      "90:	9"
    )
  }

  val shuffled0to10 = shuffle(0 to 10).toArray

  test("0 to 10 sample 5") {
    check(
      shuffled0to10,
      numToSample = 5,
      "num:	11,	mean:	5,	stddev:	3.2,	mad:	3",
      "elems:	9, 3, 7, 1, 6, …, 4, 8, 2, 0, 10",
      "sorted:	0, 1, 2, 3, 4, …, 6, 7, 8, 9, 10",
      "10:	1",
      "25:	2.5",
      "50:	5",
      "75:	7.5",
      "90:	9"
    )
  }

  test("0 to 10 sample 4") {
    check(
      shuffled0to10,
      numToSample = 4,
      "num:	11,	mean:	5,	stddev:	3.2,	mad:	3",
      "elems:	9, 3, 7, 1, …, 8, 2, 0, 10",
      "sorted:	0, 1, 2, 3, …, 7, 8, 9, 10",
      "10:	1",
      "25:	2.5",
      "50:	5",
      "75:	7.5",
      "90:	9"
    )
  }

  test("0 to 10 sample 3") {
    check(
      shuffled0to10,
      numToSample = 3,
      "num:	11,	mean:	5,	stddev:	3.2,	mad:	3",
      "elems:	9, 3, 7, …, 2, 0, 10",
      "sorted:	0, 1, 2, …, 8, 9, 10",
      "10:	1",
      "25:	2.5",
      "50:	5",
      "75:	7.5",
      "90:	9"
    )
  }

  test("0 to 10 sample 2") {
    check(
      shuffled0to10,
      numToSample = 2,
      "num:	11,	mean:	5,	stddev:	3.2,	mad:	3",
      "elems:	9, 3, …, 0, 10",
      "sorted:	0, 1, …, 9, 10",
      "10:	1",
      "25:	2.5",
      "50:	5",
      "75:	7.5",
      "90:	9"
    )
  }

  test("0 to 10 sample 1") {
    check(
      shuffled0to10,
      numToSample = 1,
      "num:	11,	mean:	5,	stddev:	3.2,	mad:	3",
      "elems:	9, …, 10",
      "sorted:	0, …, 10",
      "10:	1",
      "25:	2.5",
      "50:	5",
      "75:	7.5",
      "90:	9"
    )
  }

  test("0 to 10 sample 0") {
    check(
      shuffled0to10,
      numToSample = 0,
      "num:	11,	mean:	5,	stddev:	3.2,	mad:	3",
      "10:	1",
      "25:	2.5",
      "50:	5",
      "75:	7.5",
      "90:	9"
    )
  }

  test("0 to 100") {
    check(
      0 to 100,
      "num:	101,	mean:	50,	stddev:	29.2,	mad:	25",
      "elems:	0, 1, 2, 3, 4, 5, 6, 7, 8, 9, …, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100",
      "1:	1",
      "5:	5",
      "10:	10",
      "25:	25",
      "50:	50",
      "75:	75",
      "90:	90",
      "95:	95",
      "99:	99"
    )
  }

  test("100 to 0") {
    check(
      100 to 0 by -1,
      "num:	101,	mean:	50,	stddev:	29.2,	mad:	25",
      "elems:	100, 99, 98, 97, 96, 95, 94, 93, 92, 91, …, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0",
      "sorted:	0, 1, 2, 3, 4, 5, 6, 7, 8, 9, …, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100",
      "1:	1",
      "5:	5",
      "10:	10",
      "25:	25",
      "50:	50",
      "75:	75",
      "90:	90",
      "95:	95",
      "99:	99"
    )
  }

  val shuffledDigits = (0 until 100).map(_ ⇒ nextInt(10))

  test("100 digits") {
    check(
      shuffledDigits,
      "num:	100,	mean:	4.3,	stddev:	2.9,	mad:	2",
      "elems:	9, 6, 2, 5, 7, 9, 0, 5, 4, 6, …, 1, 9, 0×2, 8, 0, 7×2, 0, 6, 2, 4",
      "sorted:	0×15, 1×7, 2×9, 3×10, 4×10, 5×11, 6×11, 7×9, 8×9, 9×9",
      "5:	0",
      "10:	0",
      "25:	2",
      "50:	4",
      "75:	7",
      "90:	8",
      "95:	9"

    )
  }

  test("100 digits sample 4") {
    check(
      shuffledDigits,
      numToSample = 4,
      "num:	100,	mean:	4.3,	stddev:	2.9,	mad:	2",
      "elems:	9, 6, 2, 5, …, 0, 6, 2, 4",
      "sorted:	0×15, 1×7, 2×9, 3×10, …, 6×11, 7×9, 8×9, 9×9",
      "5:	0",
      "10:	0",
      "25:	2",
      "50:	4",
      "75:	7",
      "90:	8",
      "95:	9"
    )
  }

  test("100 digits sample 4 only sample sorted") {
    check(
      shuffledDigits,
      numToSample = 4,
      onlySampleSorted = true,
      "num:	100,	mean:	4.3,	stddev:	2.9,	mad:	2",
      "sorted:	0×15, 1×7, 2×9, 3×10, …, 6×11, 7×9, 8×9, 9×9",
      "5:	0",
      "10:	0",
      "25:	2",
      "50:	4",
      "75:	7",
      "90:	8",
      "95:	9"
    )
  }

  val sortedShuffledDigits = shuffledDigits.sorted

  test("100 sorted digits") {
    check(
      sortedShuffledDigits,
      "num:	100,	mean:	4.3,	stddev:	2.9,	mad:	2",
      "elems:	0×15, 1×7, 2×9, 3×10, 4×10, 5×11, 6×11, 7×9, 8×9, 9×9",
      "5:	0",
      "10:	0",
      "25:	2",
      "50:	4",
      "75:	7",
      "90:	8",
      "95:	9"
    )
  }

  test("100 sorted digits only sample sorted overridden") {
    check(
      sortedShuffledDigits,
      numToSample = 4,
      onlySampleSorted = true,
      "num:	100,	mean:	4.3,	stddev:	2.9,	mad:	2",
      "elems:	0×15, 1×7, 2×9, 3×10, …, 6×11, 7×9, 8×9, 9×9",
      "5:	0",
      "10:	0",
      "25:	2",
      "50:	4",
      "75:	7",
      "90:	8",
      "95:	9"
    )
  }

  test("values over Int.MAX_VALUE") {
    check(
      Seq(
        10000000000L,
        100000000000L,
        100000000000L,
        1000000000000L,
        1000000000000L,
        10000000000L,
        1000000000000L,
        100000000000L,
        10000000000L,
        10000000000L
      ),
      "num:	10,	mean:	334000000000,	stddev:	437588848121.2,	mad:	90000000000",
      "elems:	10000000000, 100000000000×2, 1000000000000×2, 10000000000, 1000000000000, 100000000000, 10000000000×2",
      "sorted:	10000000000×4, 100000000000×3, 1000000000000×3",
      "25:	10000000000",
      "50:	100000000000",
      "75:	325000000000"
    )
  }
}
