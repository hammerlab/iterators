package hammerlab.iterator

import org.hammerlab.iterator

import iterator.count._
trait count
  extends CountElems
     with CountByKey
object count extends count

trait either extends iterator.EitherIterator
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

import iterator.level._
trait level extends Level
object level extends level

import iterator.range._
trait range
  extends OverlappingRanges
     with Contiguous {
  type Range[T] = iterator.range.Range[T]
}
object range extends range

trait sample extends iterator.Sample
object sample extends sample

import iterator.scan._
trait scan
  extends Scan
     with ScanValues
object scan extends scan

trait slice extends iterator.Slice
object slice extends slice

import iterator.sliding._
trait sliding
  extends Sliding2
     with Sliding3
     with Sliding
object sliding extends sliding

import iterator.sorted._
trait sorted
  extends EitherZip
     with OrZip
     with Zip
object sorted extends sorted

import iterator.start._
trait start
  extends BufferedOps
     with DropEager
     with DropRight
     with HeadOption
     with NextOption
     with TakeEager
object start extends start
