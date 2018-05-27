package chapt04

import org.scalatest.{FreeSpec, Matchers}
import cats.Monad
import cats.instances.option._
import cats.instances.list._
import cats.syntax.applicative._
import cats.syntax.functor._
import cats.syntax.flatMap._
import cats.Id


class MonadsLearning extends FreeSpec with Matchers {

  trait MonadLearning[F[_]] {
    def pure[A](value: A): F[A]

    def flatMap[A, B](value: F[A])(f: A => F[B]): F[B]

    def map[A, B](value: F[A])(f: A => B): F[B] =
      flatMap(value)(a => pure(f(a)))
  }

  def sumSquare[M[_] : Monad](a: M[Int], b: M[Int]): M[Int] =
    a.flatMap(x => b.map(y => x * x + y * y))

  "scala cats monad" in {
    val opt1 = Monad[Option].pure(3)
    val opt2 = Monad[Option].flatMap(opt1)(a => Some(a + 2))
    val opt3 = Monad[Option].map(opt2)(a => 100 * a)

    opt3 should be(Some(500))

    val list1 = Monad[List].pure(3)
    val list2 = Monad[List].flatMap(List(1, 2, 3))(x => List(x, x * 10))
    val list3 = Monad[List].map(list2)(_ + 123)

    list1 should be(List(3))
    list2 should be(List(1, 10, 2, 20, 3, 30))
    list3 should be(List(124, 133, 125, 143, 126, 153))
  }

  "scala cats monad syntax" in {
    1.pure[Option] should be(Some(1))
    2.pure[List] should be(List(2))

    sumSquare(Option(3), Option(4)) should be(Some(25))
    sumSquare(List(3), List(4)) should be(List(25))
    sumSquare(List(1, 2, 3), List(4, 5)) should be(List(17, 26, 20, 29, 25, 34))
  }

  "cats id monad" in {
    sumSquare(3: Id[Int], 4: Id[Int]) should be(25: Id[Int])
  }

  "Implement id Monad" in {
    case class IdLearning[A](value: A)

    implicit def idLearning[A] = new Monad[IdLearning] {
      override def pure[A](x: A): IdLearning[A] = new IdLearning(x)

      override def flatMap[A, B](fa: IdLearning[A])(f: A => IdLearning[B]): IdLearning[B] = f(fa.value)

      override def tailRecM[A, B](a: A)(f: A => IdLearning[Either[A, B]]): IdLearning[B] = ???
    }

    Monad[IdLearning].pure(4) should be(IdLearning[Int](4))

  }

}
