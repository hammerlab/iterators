package hammerlab.iterator

import org.hammerlab.iterator

trait bulk extends iterator.Bulk
object bulk extends bulk

import iterator.count._
trait count
  extends CountElems
     with CountByKey
object count extends count

trait drop extends iterator.Drop
object drop extends drop

import iterator.group._
trait group
  extends GroupRuns
     with RunLength
     with RunLengthReencode
     with Split
object group extends group

import iterator.scan._
trait scan
  extends Scan
     with ScanValues
object scan extends scan

import iterator.sliding._
trait sliding
  extends Sliding2Iterator
     with Sliding3Iterator
     with SlidingIterator
object sliding extends sliding

import iterator.sorted._
trait sorted
  extends EitherZip
     with OrZip
     with Zip
object sorted extends sorted
