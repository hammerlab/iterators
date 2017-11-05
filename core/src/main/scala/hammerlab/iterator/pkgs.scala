package hammerlab.iterator

import org.hammerlab.iterator

trait bulk extends iterator.Bulk
object bulk extends bulk

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
  extends EitherZipIterator
     with OrZipIterator
     with ZipIterator
object sorted extends sorted