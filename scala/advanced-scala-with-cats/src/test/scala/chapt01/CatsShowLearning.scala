package chapt01

import org.scalatest.{FreeSpec, Matchers}
import cats.Show
import cats.instances.int._
import cats.instances.string._
import cats.syntax.show._

/**
  * Created by feliperojas on 11/25/17.
  */
class CatsShowLearning extends FreeSpec with Matchers {

  "cats show works like printable" in {
    val showInt: Show[Int] = Show.apply[Int]
    val showString: Show[String] = Show.apply[String]

    showInt.show(10) should be("10")
    showString.show("this is a test") should be("this is a test")
  }

  "cats show using syntax" in {
    10.show should be("10")
    "this is a test".show should be("this is a test")
  }

  "cat show implementation" in {
    final case class Cat(name: String, age: Int, color: String)
    implicit val catShow = Show.show[Cat](a => s"${a.name} is a ${a.age} years old ${a.color} cat")
    val catShow2 = Show.fromToString[Cat]

    val tobita = Cat("tobita", 10, "blue")
    tobita.show should be("tobita is a 10 years old blue cat")
    catShow2.show(tobita) should be("Cat(tobita,10,blue)")
  }

}
