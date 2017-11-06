package hammerlab

import org.hammerlab.iterator.util

/**
 * Mix-in all this module's implicits, so that all functionality can enabled via:
 *
 * {{{
 * import hammerlab.iterator._
 * }}}
 */
package object iterator
  extends count
     with end
     with iterator.either
     with group
     with level
     with map
     with ordered
     with range
     with sample
     with scan
     with slice
     with sliding
     with start {
  type SimpleIterator[+T] = util.SimpleIterator[T]
}
