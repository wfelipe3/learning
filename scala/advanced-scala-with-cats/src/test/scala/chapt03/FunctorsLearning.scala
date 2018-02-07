package chapt03

import org.scalatest.{FreeSpec, Matchers}

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

import cats.Functor
import cats.instances.list._
import cats.instances.option._
import cats.instances.function._
import cats.syntax.functor._

/**
  * Created by feliperojas on 2/6/18.
  */
class FunctorsLearning extends FreeSpec with Matchers {

  trait LearnigFunctor[F[_]] {
    def map[A, B](func: F[A])(f: A => B): F[B]
  }

  "functors have map function that convert values of the converter from one type to another" in {
    List(1, 2, 3, 4).map(_ % 2 == 0) should be(List(false, true, false, true))
    List(1, 2, 3).map(_ * 2).map(_ + 4) should be(List(1 * 2 + 4, 2 * 2 + 4, 3 * 2 + 4))
    Option(1).map(_.toString) should be(Option("1"))
    val f1 = Future("Hello world!")
    val f2 = f1.map(_.length)
    Await.result(f1, Duration.Inf) should be("Hello world!")
    Await.result(f2, Duration.Inf) should be("Hello world!".length)
  }

  "Function as a functor" in {
    val func1 = (x: Int) => x.toDouble
    val func2 = (y: Double) => y * 2
    val func3 = func1.map(func2)
    val func4 = func1 andThen func2
    val func5 = func2 compose func1

    func3(1) should be(2.0)
    func4(1) should be(2.0)
    func5(1) should be(2.0)
    func2(func1(1)) should be(2.0)
  }

  "Functor implementation" in {
    val func = new LearnigFunctor[Option] {
      def map[A, B](func: Option[A])(f: A => B) = func.map(f)
    }

    func.map(Some(0))(_ + 1) should be(Some(1))
  }

  "Cats Functor" in {
    val list = List(1, 2, 3)
    val list2 = Functor.apply[List].map(list)(_ * 2)

    list2 should be(List(2, 4, 6))

    val option = Option(123)
    val option2 = Functor.apply[Option].map(option)(_.toString)

    option2 should be(Option("123"))
  }

  "Cats functor lift - return functions with type class params and type class return" in {
    val lifted = Functor[Option].lift((x: Int) => x + 1)
    lifted(Option(1)) should be(Option(2))
  }

}
