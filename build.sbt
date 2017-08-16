name := "iterator"

version := "1.3.1-SNAPSHOT"

addScala212

deps ++=
  Seq(
    cats,
    shapeless,
    spire
  )
