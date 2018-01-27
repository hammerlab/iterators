package org.hammerlab.iterator.ordered

import org.hammerlab

abstract class Suite
  extends hammerlab.Suite {

  type L
  type R
  type Result

  def check(left: Seq[L])(right: Seq[R])(expected: Seq[Result]): Unit
}
