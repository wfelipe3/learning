package chapt01

/**
  * Created by feliperojas on 11/25/17.
  */
object CatInstance {

  final case class Cat(name: String, age: Int, color: String)

  def tobita = Cat("tobita", 10, "blue")
}
