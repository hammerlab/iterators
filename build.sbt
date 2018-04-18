
lazy val core =
  crossProject
    .settings(
      name := "iterator",
      v"2.1.0",
      // Skip compilation during doc-generation; otherwise it fails due to macro-annotations not being expanded
      emptyDocJar,
      scalameta,
      dep(
              cats,
        math.utils % "2.2.0",
             spire,
             types % "1.1.0"
      ),
      buildInfoKeys :=
        Seq[BuildInfoKey](
          name,
          version,
          scalaVersion,
          sbtVersion
        ),
      buildInfoPackage := "build_info",
      buildInfoObject := name.value
    )
    .dependsOn(
      macros
    )
    .enablePlugins(
      BuildInfoPlugin
    )
lazy val coreJS  = core.js
lazy val coreJVM = core.jvm

lazy val macros =
  crossProject
    .settings(
      subgroup("macros", "iterators"),
      v"1.0.0",
      scalameta,
      enableMacroParadise
    )
lazy val macrosJS  = macros.js
lazy val macrosJVM = macros.jvm

lazy val iterators =
  root(
      coreJS,   coreJVM,
    macrosJS, macrosJVM
  )
