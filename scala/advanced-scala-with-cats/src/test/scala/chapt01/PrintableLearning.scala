package chapt01

import org.scalatest.{FreeSpec, Matchers}
import PrintableExercise._
import PrintableExercise.PrintableInstances._
import PrintableExercise.PrintableSyntax._
import chapt01.CatInstance._

/**
  * Created by feliperojas on 11/25/17.
  */
class PrintableLearning extends FreeSpec with Matchers {

  "printable should return string for new types" in {
    Printable.format(10) should be("10")
    Printable.format("test") should be("test")
  }

  "printable for cats class" in {
    implicit val catPrintable = new Printable[Cat] {
      override def format(a: Cat): String = s"${a.name} is a ${a.age} years old ${a.color} cat"
    }

    Printable.format(tobita) should be("tobita is a 10 years old blue cat")
    tobita.format should be("tobita is a 10 years old blue cat")
    tobita.print
  }


}
