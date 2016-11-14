# iterator

[![Build Status](https://travis-ci.org/hammerlab/iterator.svg?branch=master)](https://travis-ci.org/hammerlab/iterator)
[![Coverage Status](https://coveralls.io/repos/github/hammerlab/iterator/badge.svg?branch=badge)](https://coveralls.io/github/hammerlab/iterator?branch=badge)
[![Maven Central](https://img.shields.io/maven-central/v/org.hammerlab/iterator_2.11.svg?maxAge=1800)](http://search.maven.org/#search%7Cga%7C1%7Citerator)

- Some Scala `Iterator`s and useful `Iterator`-related abstractions; see [`org.hammerlab.iterator`]().
- Also, [math](https://github.com/hammerlab/iterator/blob/master/src/main/scala/org/hammerlab/math) and [stats](https://github.com/hammerlab/iterator/blob/master/src/main/scala/org/hammerlab/stats) packages, some of which are highlighted below.

### Descriptive Statistics
[`org.hammerlab.stats.Stats`](https://github.com/hammerlab/iterator/blob/master/src/main/scala/org/hammerlab/stats/Stats.scala) has APIs for ingesting numeric elements and outputting nicely formatted statistics about them; modeled after [Apache commons-math `DescriptiveStatistics`](https://github.com/apache/commons-math/blob/MATH_3_6_1/src/main/java/org/apache/commons/math3/stat/descriptive/DescriptiveStatistics.java).

As a bonus, [it can ingest numbers in histogram-style / run-length-encoded format](https://github.com/hammerlab/magic-rdds/blob/master/src/main/scala/org/hammerlab/magic/stats/Stats.scala#L81), supporting `Long` values as well for computations involving element counts from RDDs: 

```scala
scala> import org.hammerlab.stats.Stats
scala> :paste
Stats.fromHist(
    List[(Int, Long)](
        1 ->  10000000000L,
        2 ->   1000000000,
        1 ->          100,
        2 ->   1000000000
    )
)

res0: org.hammerlab.stats.Stats[Int,Long] =
num:   	12000000100,   	mean:  	1.2,   	stddev:	0.4,   	mad:   	0
elems: 	1×10000000000, 2×1000000000, 1×100, 2×1000000000
sorted:	1×10000000100, 2×2000000000
0.0:   	1
0.1:   	1
1:     	1
5:     	1
10:    	1
25:    	1
50:    	1
75:    	1
90:    	2
95:    	2
99:    	2
99.9:  	2
100.0: 	2
```

### HyperGeometric Distribution
[`org.hammerlab.stats.HypergeometricDistribution`](https://github.com/hammerlab/iterator/blob/master/src/main/scala/org/hammerlab/stats/HypergeometricDistribution.scala) is an implementation of a hypergeometric distribution, modeled after [`org.apache.commons.math3.distribution.HypergeometricDistribution`](https://commons.apache.org/proper/commons-math/javadocs/api-3.6/org/apache/commons/math3/distribution/HypergeometricDistribution.html), but supporting 8-byte `Long` parameters.
