name := "iterator"

version := "2.0.0-SNAPSHOT"

scalaVersion := "2.11.11"

addScala212

lazy val core = project.settings(
  name := "iterator",
  version := "2.0.0-SNAPSHOT",
  enableMacroParadise,
  deps ++= Seq(
    cats,
    spire,
    types % "1.0.0-SNAPSHOT"
  )
).dependsOn(macros)

lazy val macros = project.settings(
  name := "iterator-macros",
  version := "1.0.0-SNAPSHOT",
  enableMacroParadise
)

lazy val root = rootProject("iterators-root", core, macros)
