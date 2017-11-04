name := "iterator"

version := "2.0.0-SNAPSHOT"

addScala212

lazy val macroAnnotationSettings = Seq(
  addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M10" cross CrossVersion.full),
  providedDeps += ("org.scalameta" ^^ "scalameta") % "1.8.0",
  scalacOptions += "-Xplugin-require:macroparadise",
  scalacOptions in (Compile, console) ~= (_ filterNot (_ contains "paradise")) // macroparadise plugin doesn't work in repl yet.

)

lazy val core = project.settings(
  name := "iterator",
  version := "2.0.0-SNAPSHOT",
  macroAnnotationSettings,
  deps ++= Seq(
    cats,
    spire,
    types % "1.0.0-SNAPSHOT"
  )
).dependsOn(macros)

lazy val macros = project.settings(
  name := "iterator-macros",
  version := "1.0.0-SNAPSHOT",
  macroAnnotationSettings
)

lazy val root = rootProject("iterators-root", core, macros)
