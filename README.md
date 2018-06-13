# iterators

[![Build Status](https://travis-ci.org/hammerlab/iterators.svg?branch=master)](https://travis-ci.org/hammerlab/iterators)
[![Coverage Status](https://coveralls.io/repos/github/hammerlab/iterators/badge.svg)](https://coveralls.io/github/hammerlab/iterators)
[![Maven Central](https://img.shields.io/maven-central/v/org.hammerlab/iterator_2.11.svg?maxAge=1800)](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.hammerlab%22%20AND%20a%3A%22iterator_2.11%22)

Enrichment-methods for Scala collections (`Iterator`s, `Iterable`s, and `Array`s):

```scala
import hammerlab.iterator._
```

```scala
Iterator(1, 2, 3).nextOption           // Some(1)
Iterator(1, 2, 3).buffered.headOption  // Some(1)

Array(1, 2, 1, 3).countElems
// Map(1→2, 2→1, 3→1)

List(1, 1, 2, 1, 7, 7, 7).runLengthEncode
// Iterator(1→2, 2→1, 1→1, 7→3)
```

Methods are defined in [`org.hammerlab.iterator`](core/src/main/scala/org/hammerlab/iterator) and made available for convenient importing in [`hammerlab.iterator`](core/src/main/scala/hammerlab/iterator)

## examples

by package:

### [`count`](core/src/main/scala/org/hammerlab/iterator/count)

```scala
Array(1, 2, 1, 3).countElems
// Map(1→2, 2→1, 3→1)

Iterator('a→1, 'b→2, 'a→10, 'c→3).countByKey
// Map('a→2, 'b→1, 'c→1)
```

### [`either`](core/src/main/scala/org/hammerlab/iterator/either)

```scala
def L[T](t: T) = Left(t)
def R[T](t: T) = Right(t)

Iterator(R('a), R('b), L(4)).findLeft
// Some(4)

Iterator(
  R('a),
  L( 1),
  R('b),
  R('c),
  L( 2),
  L( 3),
  R('d)
)
.groupByLeft
.mapValues(_.mkString(""))
.toList
// List((1,bc), (2,""), (3,d))
```

### [`end`](core/src/main/scala/org/hammerlab/iterator/end)

`.finish`: run a closure when the iterator is finished traversing:

```scala
import scala.io.Source.fromFile
val source = fromFile("build.sbt")
source
  .filter(_ == 'a')
  .finish({
    println("closing!")
    source.close()
  })
  .size

// 32
// prints "closing!" and closes `source` after traversal is finished
```

`.dropright`: drop `k` elements from the end of an iterator in `O(k)` space:

```scala
Iterator(1 to 10: _*).dropright(4)
// Iterator(1, 2, 3, 4, 5, 6)
```

### [`group`](core/src/main/scala/org/hammerlab/iterator/group)

Group runs of elements that satisfy a predicate or equivalence relation:

```scala
Iterator(1, 0, 2, 3, 0, 0, 4, 5, 6).groupRuns(_ > 0)
// Iterator(Iterator(1), Iterator(0), Iterator(2, 3), Iterator(0), Iterator(0), Iterator(4, 5, 6))
```

Run-length encode elements:

```scala
List(1, 1, 2, 1, 7, 7, 7).runLengthEncode
// Iterator(1→2, 2→1, 1→1, 7→3)
```

Contiguous weighted sums up to a maximum:

```scala
Iterator(1 to 6: _*).cappedCostGroups(costFn = x⇒x, limit = 10)
// Iterator(Iterator(1, 2, 3, 4), Iterator(5), Iterator(6))
```

### [`level`](core/src/main/scala/org/hammerlab/iterator/level)

Flatten a nested iterator but retain access to a cursor into unflattened version:

```scala
val it1 = Iterator(1, 2)
val it2 = Iterator(3, 4)
val it = Iterator(it1, it2).level

it.cur.get == it1
// true

it.next
// 1

it.cur.get == it1
// true

it.next
// 2

it.cur.get == it2
// true

it.next
// 3

it.cur.get == it2
// true

it.next
// 4

it.cur
// None
```
 
### [`ordered`](core/src/main/scala/org/hammerlab/iterator/ordered)

A variety of merge operations are available for sequences that are mutually ordered (possibly with respect to some 3rd type that each of their elements can be converted to).

#### `.eitherMerge`

Merge two ordered sequences using `Either`s to preserve provenance (or handle the case that the sequences' elements are not the same type):

```scala
Seq(1, 3, 4).eitherMerge(Seq(2, 3, 5, 6))
// Iterator(L(1), R(2), L(3), R(3), L(4), R(5), R(6))
```

#### `.orMerge`

Merge two ordered sequences using `Or`s:

```scala
Seq(1, 3, 4).orMerge(Seq(2, 3, 5, 6))
// Iterator(L(1), R(2), Both(3, 3), L(4), R(5), R(6))
```

#### `.leftMerge`

Collecting right-side elements for each left-side element:

```scala
Seq(1, 3, 4).leftMerge(Seq(2, 3, 5, 6))
// Iterator((1,Iterator(2)), (3,Iterator(3)), (4,Iterator(5, 6)))
```

#### `.merge`

```scala
Seq(1, 3, 4).merge(Seq(2, 3, 5, 6))
// Iterator(1, 2, 3, 3, 4, 5, 6)
```

#### Merging with a 3rd type

Instances of the `View` type-class let merges use a type other than that of the elements being merged:

```scala
// Rank a (Symbol,Int) pair using its Int value
implicit val view = View[(Symbol, Int), Int](_._2)

Seq('a→1, 'b→3).merge('c→2)
// Iterator('a→1, 'c→2, 'b→3)

Seq('a → 1, 'b → 3).eitherMerge(2)
// Iterator(L('a→1), R(2), L('b→3))
```


### [`range`](core/src/main/scala/org/hammerlab/iterator/range)

`sliceOpt`, given a `start` and `length`:
```scala
(0 to 9).sliceOpt(0,  5)
// 0 to 4

(0 to 9).sliceOpt(0, 11)
// 0 to 9

(0 to 9).sliceOpt(2, 10)
// 2 to 9

(0 to 9).sliceOpt(2,  1)
// 2 to 2
```

Also, [`.joinOverlaps`](core/src/main/scala/org/hammerlab/iterator/range/OverlappingRanges.scala) left-merges sequences of [`Range`s](core/src/main/scala/org/hammerlab/iterator/range/Range.scala), sorted by start-coordinate, based on overlaps.

### [`sample`](core/src/main/scala/org/hammerlab/iterator/sample): reservoir-sample

Reservoir-sample:

```scala
Iterator(1 to 100: _*).sample(5)
Array(15, 18, 55, 63, 98)
```

### [`scan`](core/src/main/scala/org/hammerlab/iterator/scan)

- scanL, scanR
- in terms of [`cats.Monoid`](https://typelevel.org/cats/typeclasses/monoid.html)
- optionally include the final (total/sum) element *instead of* the initial empty/zero

```scala
import hammerlab.monoid._  // some Monoid defaults

Seq(1, 2, 3, 4).scanL
// Iterator(0, 1, 3, 6)

Seq(1, 2, 3, 4).scanLeftInclusive
// Iterator(1, 3, 6, 10)

Seq(1, 2, 3, 4).scanR
// Iterator(9, 7, 4, 0)

Seq(1, 2, 3, 4).scanRightInclusive
// Iterator(10, 9, 7, 4)
```

Additionally, scan over values of kv-pairs:

```scala
Seq('a'→1, 'b'→2, 'c'→3, 'd'→4).scanLeftValues
// Iterator((a,0), (b,1), (c,3), (d,6))

Seq('a'→1, 'b'→2, 'c'→3, 'd'→4).scanLeftValuesInclusive
// Iterator((a,1), (b,3), (c,6), (d,10))

Seq('a'→1, 'b'→2, 'c'→3, 'd'→4).scanRightValues
// Iterator((a,9), (b,7), (c,4), (d,0))

Seq('a'→1, 'b'→2, 'c'→3, 'd'→4).scanRightValuesInclusive
// Iterator((a,10), (b,9), (c,7), (d,4))
```

### [`sliding`](core/src/main/scala/org/hammerlab/iterator/sliding)

Windows of size 2, including an optional next or previous element:

```scala
Seq(1, 2, 3).sliding2
// Iterator((1,2), (2,3))

Seq(1, 2, 3).sliding2Opt
// Iterator((1,Some(2)), (2,Some(3)), (3,None))

Seq(1, 2, 3).sliding2Prev
// Iterator((None,1), (Some(1),2), (Some(2),3))
```

Windows of size 3, including 2 succeeding elements, one successor and one predecessor, or full tuples only:

```scala
Seq(1, 2, 3, 4).sliding3
// Iterator((1,2,3), (2,3,4))

Seq(1, 2, 3, 4).sliding3Opt
// Iterator((None,1,Some(2)), (Some(1),2,Some(3)), (Some(2),3,Some(4)), (Some(3),4,None))

Seq(1, 2, 3, 4).sliding3NextOpts
// Iterator((1,Some(2),Some(3)), (2,Some(3),Some(4)), (3,Some(4),None), (4,None,None))
```

Windows of arbitrary size, output having same number of elems as input:

```scala
Seq(1, 2, 3, 4, 5).slide(4)
// Iterator(Seq(1, 2, 3, 4), Seq(2, 3, 4, 5), Seq(3, 4, 5), Seq(4, 5), Seq(5))
```

### [`start`](core/src/main/scala/org/hammerlab/iterator/start)

- `take`s, `drop`s, `collect`s with deterministic semantics around the iterator that was operated on
- `headOption`, `nextOption`
