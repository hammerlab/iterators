package hammerlab.iterator

import org.hammerlab.iterator
import org.hammerlab.iterator.{ EitherIterator, Slice }

trait bulk extends iterator.Bulk
object bulk extends bulk

import iterator.count._
trait count
  extends CountElems
     with CountByKey
object count extends count

trait drop extends iterator.Drop
object drop extends drop

trait either extends EitherIterator
object either extends either

import iterator.end._
trait end
  extends Finish
     with ExpandLastElement
object end extends end

import iterator.group._
trait group
  extends GroupRuns
     with RunLength
     with RunLengthReencode
     with Split
object group extends group

import iterator.range._
trait range
  extends OverlappingRangesIterator {
  type Range[T] = iterator.range.Range[T]
}
object range extends range

import iterator.scan._
trait scan
  extends Scan
     with ScanValues
object scan extends scan

trait slice extends Slice
object slice extends slice

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
