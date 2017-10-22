# iterators

[![Build Status](https://travis-ci.org/hammerlab/iterators.svg?branch=master)](https://travis-ci.org/hammerlab/iterators)
[![Coverage Status](https://coveralls.io/repos/github/hammerlab/iterators/badge.svg)](https://coveralls.io/github/hammerlab/iterators)
[![Maven Central](https://img.shields.io/maven-central/v/org.hammerlab/iterator_2.11.svg?maxAge=1800)](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.hammerlab%22%20AND%20a%3A%22iterator_2.11%22)

Scala `Iterator`s and useful `Iterator`-related abstractions; see [`org.hammerlab.iterator`]().

### [Scans](src/main/scala/org/hammerlab/iterator/scan):
- in terms of [`Monoid`](https://typelevel.org/cats/typeclasses/monoid.html)
- {left, right}
- {inclusive, exclusive} of first element
- elements or values of tuples

### [Sliding / Windowed traversals](src/main/scala/org/hammerlab/iterator/sliding)
- for 2- and 3-tuples
- `Option`-al next/prev elements
- [run-length encoding](src/main/scala/org/hammerlab/iterator/RunLengthIterator.scala)
- [grouping consecutive elements by predicate / `Ordering`](src/main/scala/org/hammerlab/iterator/GroupRunsIterator.scala)

### Drops / Takes
- [eager drop](src/main/scala/org/hammerlab/iterator/DropRightIterator.scala)
- [eager take](src/main/scala/org/hammerlab/iterator/TakeEagerIterator.scala)
- [takewhile / dropwhile / collectwhile](src/main/scala/org/hammerlab/iterator/bulk/BufferedBulkIterator.scala)

Context: [scala/bug#9274](https://github.com/scala/bug/issues/9274#issuecomment-308218901)

### Sorted/Range zips
- [in terms of `Either`, `Or`](src/main/scala/org/hammerlab/iterator/sorted)
- [as a mapping from each left-side element to a list of right-side elements](src/main/scala/org/hammerlab/iterator/GroupWithIterator.scala)
- [join iterators of ranges by overalap](src/main/scala/org/hammerlab/iterator/range/OverlappingRangesIterator.scala)

### [`SimpleBufferedIterator`](src/main/scala/org/hammerlab/iterator/SimpleBufferedIterator.scala)
- define iterators in terms of only a `nextOption` that returns an `Option`
- `hasNext` lazily buffers and caches `head`

### And more…
- [`.nextOption` / `.headOption`](src/main/scala/org/hammerlab/iterator/package.scala)
- [`Iterator[Either]`](src/main/scala/org/hammerlab/iterator/EitherIterator.scala) utilities
- [`.finish`](src/main/scala/org/hammerlab/iterator/FinishingIterator.scala): clean up / teardown hook
