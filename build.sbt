lazy val core = crossProject.settings(
  name := "iterator",
  v"2.1.0",
  scalameta,
  // Skip compilation during doc-generation; otherwise it fails due to macro-annotations not being expanded
  emptyDocJar,
  dep(
    cats,
    spire,
    types % "1.0.2".snapshot
  )
).dependsOn(
  macros
)
lazy val coreJS  = core.js
lazy val coreJVM = core.jvm

lazy val macros = crossProject.settings(
  name := "iterator-macros",
  v"1.2.0",
  scalameta
)
lazy val macrosJS  = macros.js
lazy val macrosJVM = macros.jvm

lazy val iterators = rootProject(
  "iterators",
  coreJS, coreJVM,
  macrosJS, macrosJVM
)
