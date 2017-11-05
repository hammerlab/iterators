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
      case q"class $name[..$ts]($param) { ..$body }" =>
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

        q"""trait $name {
              class $ops[..$ts]($param) {
                ..$body
              }
              implicit def $makeName[..$ts](it: scala.Iterator[$tn]): $ops[..$tns] = new $opsCtor($iterArg)
              implicit def $makeName[..$ts](it: scala.Iterable[$tn]): $ops[..$tns] = new $opsCtor($arrArg)
              implicit def $makeName[..$ts](it: scala.Array[$tn]): $ops[..$tns] = new $opsCtor($arrArg)
            }
          """
      case _ =>
        abort("Didn't recognize constructor form of annotated object")
    }
  }
}
