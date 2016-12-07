
name := "iterator"

version := "1.1.0"

libraryDependencies ++= Seq(
  libraries.value('kryo),
  libraries.value('spire),
  "org.apache.commons" % "commons-math3" % "3.6.1"
)

testDeps += libraries.value('test_utils)
