package hammerlab.iterators

import org.hammerlab.iterator

trait bulk extends iterator.Bulk
object bulk extends bulk

trait scan
  extends iterator.scan.Scan
    with iterator.scan.ScanValues

object scan extends scan
