package org.hammerlab.iterator.ordered

import org.hammerlab.test

abstract class Suite
  extends test.Suite {

  type L
  type R
  type Result

  def check(left: Seq[L])(right: Seq[R])(expected: Seq[Result]): Unit
}
