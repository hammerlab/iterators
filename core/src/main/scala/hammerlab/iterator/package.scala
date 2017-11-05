package hammerlab

import org.hammerlab.iterator.SimpleBufferedIterator

package object iterator
  extends count
     with end
     with iterator.either
     with group
     with range
     with scan
     with slice
     with sliding
     with sorted
     with start {
  type SimpleIterator[+T] = SimpleBufferedIterator[T]
}
