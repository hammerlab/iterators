package org.hammerlab.iterator.sorted

import org.hammerlab.test

abstract class Suite
  extends test.Suite {

  type L
  type R
  type Result

  def check(left: Seq[L])(right: Seq[R])(expected: Seq[Result]): Unit
}
