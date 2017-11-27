package chapt01

/**
  * Created by feliperojas on 11/25/17.
  */
object PrintableExercise {

  trait Printable[A] {
    def format(a: A): String
  }

  object PrintableInstances {

    implicit val stringPrintable = new Printable[String] {
      override def format(a: String): String = a
    }

    implicit val intPrintable = new Printable[Int] {
      override def format(a: Int): String = a.toString
    }

  }

  object Printable {

    def format[A: Printable](a: A): String =
      implicitly[Printable[A]].format(a)

    def print[A: Printable](a: A): Unit =
      println(implicitly[Printable[A]].format(a))

  }

  object PrintableSyntax {

    implicit class PrintOps[A](value: A) {
      def format(implicit printable: Printable[A]) = printable.format(value)

      def print(implicit printable: Printable[A]) = println(printable.format(value))
    }

  }

}
