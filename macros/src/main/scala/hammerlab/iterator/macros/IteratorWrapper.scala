package hammerlab.iterator.macros

import scala.annotation.StaticAnnotation
import scala.collection.immutable.Seq
import scala.meta.Term.Param
import scala.meta.Type.{Apply, Name}
import scala.meta._

class IteratorWrapper
  extends StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    defn match {
      case q"class $name[..$ts]($param) { ..$body }" ⇒
        val ops = Name(s"${name}Ops")
        val opsCtor = Ctor.Ref.Name(ops.value)
        val makeName = Term.Name(s"make$name")

        val tns = ts.map(t ⇒ Name(t.name.value))

        val Param(
          _,  // mods
          _,  // name
          Some(
            Apply(
              iterType,  // Iterator or BufferedIterator
              Seq(tn)    // Type arg or tuple of two type args
            )
          ),
          _   // "default"
        ) = param

        val Name(iterTypeName) = iterType

        val (iterArg, arrArg) =
          if (iterTypeName == "BufferedIterator")
            (q"it.buffered", q"it.iterator.buffered")
          else
            (q"it", q"it.iterator")

        val classDef =
          if (ts.isEmpty)
            q"class $ops($param) { ..$body }"
          else
            q"class $ops[..$ts]($param) { ..$body }"

        def makeImplicit(argType: String, iterator: Term) = {
          val arg = Apply(Name(argType), Seq(tn))
          val ctor = q"new $opsCtor($iterator)"
          if (ts.isEmpty)
            q"implicit def $makeName(it: $arg): $ops = $ctor"
          else
            q"implicit def $makeName[..$ts](it: $arg): $ops[..$tns] = $ctor"
        }

        val iteratorImplicit = makeImplicit("scala.Iterator", iterArg)
        val iterableImplicit = makeImplicit("scala.Iterable",  arrArg)
        val    arrayImplicit = makeImplicit("scala.Array"   ,  arrArg)

        q"""trait $name {
              $classDef
              $iteratorImplicit
              $iterableImplicit
              $arrayImplicit
            }
          """
      case _ ⇒
        abort("Didn't recognize constructor form of annotated object")
    }
  }
}
