package hammerlab.iterator.macros

import scala.annotation.StaticAnnotation
import scala.annotation.compileTimeOnly
import scala.language.experimental.macros
import scala.reflect.macros.whitebox

@compileTimeOnly("enable macro paradise to expand macro annotations")
class IteratorWrapper
  extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro IteratorWrapper.impl
}

object IteratorWrapper {
  def impl(c: whitebox.Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._
    val inputs = annottees.map(_.tree).toList

    val (expr, rest) =
      inputs match {
        case q"class $name[$t]($param) { ..$body }" :: rest ⇒

          val makeName = TermName(s"make$name")
          val ops = TypeName(s"${name}Ops")

          val expr =
            c.Expr[Any](
              q"""
                trait $name {
                  class $ops[$t]($param) {
                    ..$body
                  }

                  implicit def $makeName[T](it: scala.collection.Iterator[T]): $ops[T] = new this.$ops(it)
                  implicit def $makeName[T](it: scala.collection.Iterable[T]): $ops[T] = new this.$ops(it.iterator)
                  implicit def $makeName[T](it: scala.Array[T]): $ops[T] = new this.$ops(it.iterator)
                }
              """
            )

          (expr, rest)


        case q"class $name[$k, $v]($it: $itt[$kv]) { ..$body }" :: rest ⇒
          val TypeDef(_, kn, _, _) = k
          val TypeDef(_, vn, _, _) = v

          val makeName = TermName(s"make$name")
          val ops = TypeName(s"${name}Ops")

          val expr =
            c.Expr[Any](
              q"""
                trait $name {
                  class $ops[$kn, $vn](it: scala.Iterator[$kv]) {
                    ..$body
                  }

                  implicit def $makeName[$k, $v](it: scala.collection.Iterator[$kv]): $ops[$kn, $vn] = new this.$ops(it)
                  implicit def $makeName[$k, $v](it: scala.collection.Iterable[$kv]): $ops[$kn, $vn] = new this.$ops(it.iterator)
                  implicit def $makeName[$k, $v](it: scala.Array[$kv]): $ops[$kn, $vn] = new this.$ops(it.iterator)
                }
                """
            )

          (expr, rest)
        case _ ⇒
          throw new Exception(s"expected ClassDef: $inputs")
      }

    c.Expr[Any](
      Block(
        expr.tree :: rest,
        Literal(Constant(()))
      )
    )
  }
}
