package org.hammerlab

import spire.math.Integral

package object math {
  /**
   * Simple helper for rounding-up integer-division
   */
  def ceil[N: Integral](numerator: N, denominator: N): N = {
    val numeric = implicitly[Integral[N]]
    import numeric._
    fromDouble(
      scala.math.ceil(
        toDouble(numerator) / toDouble(denominator)
      )
    )
  }
}
