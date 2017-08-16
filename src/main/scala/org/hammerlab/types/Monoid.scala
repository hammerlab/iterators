package org.hammerlab.types

import shapeless._

object Monoid extends ProductTypeClassCompanion[cats.Monoid] {
  object typeClass extends ProductTypeClass[cats.Monoid] {
    def emptyProduct = new cats.Monoid[HNil] {
      def empty = HNil
      def combine(a: HNil, b: HNil) = HNil
    }

    def product[F, T <: HList](mh: cats.Monoid[F], mt: cats.Monoid[T]) = new cats.Monoid[F :: T] {
      def empty = mh.empty :: mt.empty
      def combine(a: F :: T, b: F :: T) = mh.combine(a.head, b.head) :: mt.combine(a.tail, b.tail)
    }

    def project[F, G](instance: => cats.Monoid[G], to: F => G, from: G => F) = new cats.Monoid[F] {
      def empty = from(instance.empty)
      def combine(a: F, b: F) = from(instance.combine(to(a), to(b)))
    }
  }
}
