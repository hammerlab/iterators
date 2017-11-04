package hammerlab.iterator.macros

import scala.annotation.StaticAnnotation
import scala.meta._
import scala.collection.immutable.Seq

class IteratorWrapper
  extends StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    defn match {
      case q"class $name[..$ts]($param) { ..$body }" =>
        val ops = Type.Name(s"${name}Ops")
        val opsCtor = Ctor.Ref.Name(ops.value)
        val makeName = Term.Name(s"make$name")

        val tns = ts.map(t ⇒ Type.Name(t.name.value))

        val tn: Type =
          tns match {
            case t :: Nil ⇒ t
            case t1 :: t2 :: Nil ⇒
              Type.Apply(
                Type.Name("scala.Tuple2"),
                Seq[Type](t1, t2)
              )
            case names ⇒
              throw new IllegalArgumentException(
                s"Expected one or two type parameters, found ${names.size}: ${names.mkString(",")}"
              )
          }

        q"""trait $name {
              class $ops[..$ts]($param) {
                ..$body
              }
              implicit def $makeName[..$ts](it: scala.Iterator[$tn]): $ops[..$tns] = new $opsCtor(it)
              implicit def $makeName[..$ts](it: scala.Iterable[$tn]): $ops[..$tns] = new $opsCtor(it.iterator)
              implicit def $makeName[..$ts](it: scala.Array[$tn]): $ops[..$tns] = new $opsCtor(it.iterator)
            }
          """
      case _ =>
        abort("Didn't recognize constructor form of annotated object")
    }
  }
}
