package hammerlab.iterator.macros

import scala.annotation.StaticAnnotation
import scala.meta._

/**
 * Annotation for a trait that generates a [[Serializable]] companion object that mixes in the trait.
 *
 * Useful for bags of mix-ins comprising import APIs; see [[hammerlab.iterator]]
 */
class obj
  extends StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    defn match {
      case q"trait $name extends $template with ..$mixins { ..$body }" ⇒
        // Default case: no companion object; generate an empty Serializable companion
        val objName = Term.Name(s"$name")
        val ctor = Ctor.Ref.Name(s"$name")
        q"""
            trait $name extends $template with ..$mixins { ..$body }
            object $objName extends $ctor with Serializable {}
         """
      case q"trait $name extends $template with ..$mixins { ..$body }; object $objName { ..$comp }" ⇒
        // Given an existing companion object, add mix-ins of the trait and Serializable
        val ctor = Ctor.Ref.Name(s"$name")
        q"""
            trait $name extends $template with ..$mixins { ..$body }
            object $objName extends $ctor with Serializable { ..$comp }
         """
      case _ ⇒
        abort("Expected trait definition")
    }
  }
}
