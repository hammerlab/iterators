package org.hammerlab.iterator.sorted

trait IntsTest {

  self: Suite with VInt ⇒

  type L = Int
  type R = Int

  def L(t: L): Result
  def R(u: R): Result

  def expected: Map[String, Seq[Result]]

  def test(l: Int*)(r: Int*): Unit = {
    def str(n: Seq[Int]): String =
      if (n.isEmpty)
        "empty"
      else
        n.mkString(",")

    val name = s"${str(l)} ${str(r)}"

    test(name) {
      check(l)(r)(expected(name))
    }
  }

  test(
    1, 3, 5
  )(
    2, 4, 6
  )

  test(
    1, 2, 3
  )(
    4, 5, 6
  )

  test(
    1, 2, 3
  )(
    1, 2, 3
  )

  test(
    1, 2, 4, 7, 9
  )(
    1, 3, 5, 6, 7, 8
  )

  test()()

  test()(1)

  test(

  )(
    1, 10, 100
  )

  test(1)()

  test(
    1, 10, 100
  )(

  )
}
