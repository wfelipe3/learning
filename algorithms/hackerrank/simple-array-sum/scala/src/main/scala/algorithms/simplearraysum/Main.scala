package algorithms.simplearraysum

/**
  * Created by feliperojas on 2/3/18.
  */
object Main extends App {

  val res = io.Source.stdin
    .getLines()
    .take(2)
    .toList
    .tail
    .flatMap(_.split(" "))
    .map(_.toInt)
    .sum

  println(res)
}
