package org.hammerlab.iterator

class BuildTest
  extends hammerlab.Suite {
  test("build") {
    ==(build_info.iterator.version, "2.3.0-SNAPSHOT")
  }
}
