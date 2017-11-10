package hammerlab.iterator

import hammerlab.iterator.macros.obj
import org.hammerlab.iterator

import iterator.count._
@obj trait count
  extends CountElems
     with CountByKey

@obj trait either extends iterator.either.EitherIterator

import iterator.end._
@obj trait end
  extends Finish
     with ExpandLastElement
     with DropRight

import iterator.group._
@obj trait group
  extends CappedCost
     with GroupRuns
     with RunLength
     with RunLengthReencode
     with Split

@obj trait level extends iterator.level.Level

@obj trait map extends iterator.map.MapValues

import iterator.ordered._
@obj trait ordered
  extends EitherMerge
     with LeftMerge
     with OrMerge
     with Merge {
  type View[-From, +To] = iterator.ordered.View[From, To]
  val View = iterator.ordered.View
}

import iterator.range._
@obj trait range
  extends OverlappingRanges
     with Contiguous
object range {
  /**
   * Leave these aliases out of the [[range]] trait above (which gets mixed into [[hammerlab.iterator]]) because we want
   * to minimize chances of conflict with [[scala.Range]]
   */
  type Range[T] = iterator.range.Range[T]
  val Range = iterator.range.Range
}

@obj trait sample extends iterator.sample.Sample

import iterator.scan._
@obj trait scan
  extends Scan
     with ScanValues

@obj trait slice extends Slice

import iterator.sliding._
@obj trait sliding
  extends Sliding2
     with Sliding3
     with Sliding

import iterator.start._
@obj trait start
  extends BufferedOps
     with DropEager
     with HeadOption
     with NextOption
     with TakeEager
