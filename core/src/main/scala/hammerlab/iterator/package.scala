package hammerlab

package object iterator
  extends count
     with end
     with iterator.either
     with group
     with level
     with range
     with sample
     with scan
     with slice
     with sliding
     with sorted
     with start {
  type SimpleIterator[+T] = org.hammerlab.iterator.SimpleIterator[T]
}
