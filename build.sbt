
lazy val core = crossProject.settings(
  name := "iterator",
  v"2.1.0",
  // Skip compilation during doc-generation; otherwise it fails due to macro-annotations not being expanded
  emptyDocJar,
  dep(
          cats,
    math.utils % "2.2.0",
         spire,
         types % "1.1.0"
  )
).dependsOn(
  macros
)
lazy val coreJS  = core.js
lazy val coreJVM = core.jvm

lazy val macros = crossProject.settings(
  group("org.hammerlab.macros"),
  name := "iterators",
  v"1.0.0"
)
lazy val macrosJS  = macros.js
lazy val macrosJVM = macros.jvm

default(scalameta)

lazy val iterators = rootProject(
  "iterators",
    coreJS,   coreJVM,
  macrosJS, macrosJVM
)
