package org.hammerlab.iterator.sorted

import org.hammerlab.test.Suite

abstract class ZipIteratorTest
  extends Suite {

  type L
  type R
  type Result

  def check(left: L*)(right: R*)(expected: Result*): Unit
}
