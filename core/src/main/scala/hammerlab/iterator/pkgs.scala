package hammerlab.iterator

import org.hammerlab.iterator

import iterator.count._
trait count
  extends CountElems
     with CountByKey
object count extends count

import iterator.either.EitherIterator
trait either extends EitherIterator
object either extends either

import iterator.end._
trait end
  extends Finish
     with ExpandLastElement
     with DropRight
object end extends end

import iterator.group._
trait group
  extends CappedCost
     with GroupRuns
     with RunLength
     with RunLengthReencode
     with Split
object group extends group

import iterator.level._
trait level extends Level
object level extends level

trait map extends iterator.map.MapValues
object map extends map

import iterator.ordered._
trait ordered
  extends EitherMerge
     with LeftMerge
     with OrMerge
     with Merge
object ordered extends ordered

import iterator.range._
trait range
  extends OverlappingRanges
     with Contiguous {
  type Range[T] = iterator.range.Range[T]
}
object range extends range

import iterator.sample.Sample
trait sample extends Sample
object sample extends sample

import iterator.scan._
trait scan
  extends Scan
     with ScanValues
object scan extends scan

trait slice extends Slice
object slice extends slice

import iterator.sliding._
trait sliding
  extends Sliding2
     with Sliding3
     with Sliding
object sliding extends sliding

import iterator.start._
trait start
  extends BufferedOps
     with DropEager
     with HeadOption
     with NextOption
     with TakeEager
object start extends start
