package org.hammerlab.stats

import cats.Show
import cats.Show.show
import cats.instances.all.catsStdShowForString
import cats.syntax.all._
import org.hammerlab.iterator.RunLengthIterator._
import org.hammerlab.math.interpolate
import org.hammerlab.types._
import spire.implicits._
import spire.math.{ Integral, Numeric, Rational }

import scala.Double.NaN
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.math.{ abs, ceil, floor, sqrt }

sealed abstract class StatsI[K: Numeric, V: Integral]

object StatsI {
  implicit def makeShow[
    K : Numeric : Show,
    V: Integral : Show
  ](
    implicit
    percentileShow: Show[Rational] = showPercentile,
    statShow: Show[Double] = showDouble
  ): Show[StatsI[K, V]] =
    show {
      case Empty() ⇒ "(empty)"
      case Stats(n, mean, stddev, _, mad, samplesOpt, sortedSamplesOpt, percentiles) ⇒
        def pair[L: Show, R: Show](l: L, r: R): String =
          show"$l:\t$r"

        val strings = ArrayBuffer[String]()

        strings +=
          List(
            pair("num", n),
            pair("mean", mean),
            pair("stddev", stddev),
            pair("mad", mad)
          )
          .mkString(",\t")

        for {
          samples ← samplesOpt
          if samples.nonEmpty
        } {
          strings += pair("elems", samples)
        }

        for {
          sortedSamples ← sortedSamplesOpt
          if sortedSamples.nonEmpty
        } {
          strings += pair("sorted", sortedSamples)
        }

        strings ++=
          percentiles.map {
            case (k, v) ⇒
              pair(k, v)
          }

        strings.mkString("\n")
    }

  def showDouble: Show[Double] =
    show(
      d ⇒
        if (floor(d).toLong == ceil(d).toLong)
          d.toLong.toString
        else
          "%.1f".format(d)
    )

  def showPercentile: Show[Rational] =
    show(
      r ⇒
        if (r.isWhole())
          r.toLong.toString
        else
          r.toDouble.toString
    )
}

case class Empty[K: Numeric, V: Integral]() extends StatsI[K, V]

/**
 * Wrapper for some computed statistics about a dataset of [[Numeric]] elements.
 *
 * @param n number of elements in the dataset.
 * @param mad median absolute deviation (from the median).
 * @param samplesOpt "sample" elements; the start and end of the data.
 * @param sortedSamplesOpt "sample" elements; the least and greatest elements. If the dataset is already sorted, meaning
 *                         this would be equivalent to [[samplesOpt]], it is omitted.
 * @param percentiles selected percentiles of the dataset.
 * @tparam K [[Numeric]] element type. TODO(ryan): allow this to be non-[[Numeric]].
 * @tparam V [[Integral]] value type.
 */
case class Stats[K: Numeric, V: Integral](n: V,
                                          mean: Double,
                                          stddev: Double,
                                          median: Double,
                                          mad: Double,
                                          samplesOpt: Option[Samples[K, V]],
                                          sortedSamplesOpt: Option[Samples[K, V]],
                                          percentiles: Seq[(Rational, Double)])
  extends StatsI[K, V]

/**
 * Helpers for constructing [[Stats]] / computing the statistics that populate a [[Stats]] instance.
 */
object Stats {

  /**
   * Construct a [[Stats]] from a sequence of "runs"; elements paired with a count of repetitions.
   *
   * @param v values.
   * @param numToSample highlight this many "runs" of data from the start and end of the data; likewise the least and
   *                    greatest elements (and repetition counts).
   * @param onlySampleSorted only highlight the least and greatest elements; omit the first and last
   */
  def fromHist[K: Numeric: Ordering, V: Integral](v: Iterable[(K, V)],
                                                  numToSample: Int = 10,
                                                  onlySampleSorted: Boolean = false): StatsI[K, V] = {

    var alreadySorted = true
    val hist = mutable.HashMap[K, V]()
    var n = Integral[V].zero

    val values = {
      val vBuilder = Vector.newBuilder[(K, V)]
      var prevOpt: Option[K] = None
      for {
        (value, num) ← reencode[K, V](v.iterator)
      } {
        if (alreadySorted) {
          if (prevOpt.exists(_ > value))
            alreadySorted = false
          else
            prevOpt = Some(value)
        }
        vBuilder += value → num
        n += num
        hist.update(value, hist.getOrElse(value, Integral[V].zero) + num)
      }

      vBuilder.result()
    }

    if (values.isEmpty)
      return Empty[K, V]()

    val sorted =
      if (alreadySorted)
        values
      else
        for {
          key ← hist.keys.toVector.sorted
        } yield
          key → hist(key)

    val percentiles = histPercentiles(n, sorted)

    val median = percentiles(percentiles.length / 2)._2

    val medianDeviationsBuilder = Vector.newBuilder[(Double, V)]

    var sum = 0.0
    var sumSquares = 0.0
    for ((value, num) ← sorted) {
      val d = value.toDouble
      sum += d * num.toDouble()
      sumSquares += d * d * num.toDouble()
      medianDeviationsBuilder += abs(d - median) → num
    }

    val medianDeviations = medianDeviationsBuilder.result().sortBy(_._1)

    val mad =
      getRunPercentiles(
        medianDeviations,
        Seq(
          Rational(50) →
            ((n.toDouble() - 1) / 2.0)
        )
      )
      .head
      ._2

    val mean = sum / n.toDouble()
    val stddev = sqrt(sumSquares / n.toDouble() - mean * mean)

    def samples(vs: Vector[(K, V)]): Samples[K, V] =
      Samples[K, V](
        n,
        vs.take(numToSample),
        vs.takeRight(numToSample)
      )

    val samplesOpt =
      (alreadySorted || !onlySampleSorted) |
        samples(values)

    val sortedSamplesOpt =
      !alreadySorted |
        samples(sorted)

    Stats(
      n,
      mean, stddev,
      median, mad,
      samplesOpt,
      sortedSamplesOpt,
      percentiles
    )
  }

  /**
   * Construct a [[Stats]] instance from input data `v`.
   * @param v values.
   * @param numToSample highlight this many "runs" of data from the start and end of the data; likewise the least and
   *                    greatest elements (and repetition counts).
   * @param onlySampleSorted only highlight the least and greatest elements; omit the first and last
   */
  def apply[K: Numeric: Ordering](v: Iterable[K],
                                  numToSample: Int = 10,
                                  onlySampleSorted: Boolean = false): StatsI[K, Int] = {

    val vBuilder = Vector.newBuilder[K]
    var alreadySorted = true
    var prevOpt: Option[K] = None
    for (value ← v) {
      if (alreadySorted) {
        if (prevOpt.exists(_ > value))
          alreadySorted = false
        else
          prevOpt = Some(value)
      }
      vBuilder += value
    }

    val values = vBuilder.result()

    if (values.isEmpty)
      return Empty[K, Int]()

    val n = values.length

    val sorted =
      if (alreadySorted)
        values
      else
        values.sorted

    val median = getMedian(sorted)

    val medianDeviationsBuilder = Vector.newBuilder[Double]

    var sum = 0.0
    var sumSquares = 0.0
    for (value ← sorted) {
      val d = value.toDouble
      sum += d
      sumSquares += d * d
      medianDeviationsBuilder += abs(d - median)
    }

    val medianDeviations = medianDeviationsBuilder.result().sorted
    val mad = getMedian(medianDeviations)

    val mean = sum / n
    val stddev = sqrt(sumSquares / n - mean * mean)

    def samples(vs: Vector[K]): Samples[K, Int] = {
      // Count occurrences of the first N distinct values.
      val (firstElems, numFirstElems) =
        runLengthEncodeWithSum(
          vs.iterator,
          numToSample
        )

      // Count occurrences of the last N distinct values.
      val (lastElems, numLastElems) =
        runLengthEncodeWithSum(
          vs.reverseIterator,
          numToSample,
          reverse = true
        )

      Samples(
        n,
        Runs(firstElems, numFirstElems),
        Runs(lastElems, numLastElems)
      )
    }

    val samplesOpt =
      (alreadySorted || !onlySampleSorted) |
        samples(values)

    val sortedSamplesOpt =
      !alreadySorted |
        samples(sorted)

    new Stats(
      n,
      mean, stddev,
      median, mad,
      samplesOpt,
      sortedSamplesOpt,
      percentiles(sorted)
    )
  }

  /**
   * Compute percentiles listed in `ps` of the data in `values`; wrapper for implementation below.
   */
  private def getRunPercentiles[K: Numeric, V: Integral](values: Seq[(K, V)],
                                                         ps: Seq[(Rational, Double)]): Vector[(Rational, Double)] =
    getRunPercentiles(
      values
        .iterator
        .buffered,
      ps
        .iterator
        .buffered
    )
    .toVector

  /**
   * Compute percentiles listed in `ps` of the data in `values`.
   *
   * @param values runs of elements.
   * @param percentiles percentiles to compute, specified as tuples where the key is the percentile and the value is the
   *                    index in `values` at which that percentile lies (interpolated to be a fractional amount between
   *                    two indices, where appropriate).
   * @return pairs of (percentile, value).
   */
  private def getRunPercentiles[K: Numeric, V: Integral](values: BufferedIterator[(K, V)],
                                                         percentiles: BufferedIterator[(Rational, Double)]): Iterator[(Rational, Double)] =
    new Iterator[(Rational, Double)] {

      var elemsPast = 0.0
      var curK: Option[Double] = None

      override def hasNext: Boolean = percentiles.hasNext

      override def next(): (Rational, Double) = {
        val (percentile, idx) = percentiles.next()
        while(elemsPast <= idx) {
          val (k, v) = values.next()
          curK = Some(k.toDouble())
          elemsPast += v.toDouble()
        }

        val distancePast = elemsPast - idx

        percentile →
          (
            if (distancePast < 1)
              curK.get * distancePast + values.head._1.toDouble() * (1 - distancePast)
            else
              curK.get
          )
      }
    }

  /**
   * Compute some relevant percentiles based on the number of elements present.
   * @return pairs of (percentile, value).
   */
  private def histPercentiles[K: Numeric, V: Integral](N: V,
                                                       values: IndexedSeq[(K, V)]): Vector[(Rational, Double)] = {
    val n = N - 1
    val denominators: Iterator[Int] = Iterator(2, 4, 10, 20, 100, 1000, 10000)

    val nd = n.toDouble
    val percentileIdxs =
      denominators
        .takeWhile(d ⇒ d <= n || d == 2)  // Always take the median (denominator 2 aka 50th percentile).
        .flatMap {
          d ⇒
            val loPercentile = Rational(100, d)
            val hiPercentile = 100 - loPercentile

            val loIdx = nd / d
            val hiIdx = nd - loIdx

            if (d == 2)
            // Median (50th percentile, denominator 2) only emits one tuple.
              Iterator(loPercentile → loIdx)
            else
            // In general, we emit two tuples per "denominator", one on the high side and one on the low. For example, for
            // denominator 4, we emit the 25th and 75th percentiles.
              Iterator(loPercentile → loIdx, hiPercentile → hiIdx)
        }
        .toArray
        .sortBy(_._1)

    getRunPercentiles(values, percentileIdxs)
  }

  /**
   * Compute some relevant percentiles based on the number of elements present.
   *
   * @return pairs of (percentile, value).
   */
  private def percentiles[T: Numeric](values: IndexedSeq[T]): Vector[(Rational, Double)] = {
    val n = values.length + 1

    val denominators: Iterator[Int] = {
      lazy val pow10s: Stream[Int] = 100 #:: pow10s.map(_ * 10)
      Iterator(
         2,  // 50
         4,  // 25/75
        10,  // 10/90
        20   //  5/95
      ) ++ pow10s.iterator  // 1/99, .1/99.9, .01/99.99, …
    }

    denominators
      .takeWhile(_ <= n)
      .flatMap {
        d ⇒
          val loPercentile = Rational(100, d)
          val hiPercentile = 100 - loPercentile

          val loFloor = n / d - 1
          val loRemainder = n % d

          val hiCeil = n - 2 - loFloor

          val (lo, hi) =
            if (loRemainder == 0)
              (
                values(loFloor).toDouble(),
                values( hiCeil).toDouble()
              )
            else {
              val floorWeight = loRemainder.toDouble() / d
              (
                interpolate(values(loFloor), values(loFloor + 1), floorWeight),
                interpolate(values( hiCeil), values( hiCeil - 1), floorWeight)
              )
            }

          if (d == 2)
            // Median (50th percentile, denominator 2) only emits one tuple.
            Iterator(loPercentile → lo)
          else
            // In general, we emit two tuples per "denominator", one on the high side and one on the low. For example, for
            // denominator 4, we emit the 25th and 75th percentiles.
            Iterator(loPercentile → lo, hiPercentile → hi)
        }
      .toVector
      .sortBy(_._1)
  }

  private def getMedian[T: Numeric](sorted: Vector[T]): Double = {
    val n = sorted.length
    if (n == 0)
      NaN
    else if (n % 2 == 0)
      (sorted(n / 2 - 1) + sorted(n / 2)).toDouble() / 2.0
    else
      sorted(n / 2).toDouble()
  }

  /**
   * Find the first `N` "runs" from the beginning of `it`. If `reverse`, return them in reversed order.
   */
  private def runLengthEncodeWithSum[K: Numeric](it: Iterator[K],
                                                 N: Int,
                                                 reverse: Boolean = false): (Seq[(K, Int)], Int) = {
    var sum = 0
    var i = 0
    val runs = ArrayBuffer[(K, Int)]()
    val runLengthIterator = it.runLengthEncode()
    while (i < N && runLengthIterator.hasNext) {
      val (elem, count) = runLengthIterator.next()

      if (reverse)
        runs.prepend(elem → count)
      else
        runs += ((elem, count))

      sum += count
      i += 1
    }
    runs → sum
  }
}
