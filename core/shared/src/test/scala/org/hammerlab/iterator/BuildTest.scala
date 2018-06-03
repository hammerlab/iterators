package org.hammerlab.iterator

class BuildTest
  extends hammerlab.Suite {
  test("build") {
    ==(build_info.iterator.version, "2.1.0-SNAPSHOT")
  }
}
