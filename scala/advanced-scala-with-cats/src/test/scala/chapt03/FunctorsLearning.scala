package chapt03

import org.scalatest.{FreeSpec, Matchers}

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import cats.Functor
import cats.instances.list._
import cats.instances.option._
import cats.instances.function._
import cats.syntax.functor._

import scala.util.Try

/**
  * Created by feliperojas on 2/6/18.
  */
class FunctorsLearning extends FreeSpec with Matchers {

  trait LearningFunctor[F[_]] {
    def map[A, B](func: F[A])(f: A => B): F[B]
  }

  object LearningFunctor {
    def apply[F[_]](implicit learnigFunctor: LearningFunctor[F]) =
      learnigFunctor
  }

  object LearningFunctorsSyntax {

    implicit class FunctorOps[F[_], A](value: F[A]) {
      def mapLearn[B](f: A => B)(implicit functor: LearningFunctor[F]) =
        functor.map(value)(f)
    }

  }

  object FunctorInstances {
    implicit val optionFunctor = new LearningFunctor[Option] {
      def map[A, B](func: Option[A])(f: A => B) = func.map(f)
    }
  }

  trait Printable[A] {
    val self = this

    def format(value: A): String

    def contramap[B](func: B => A): Printable[B] = (value: B) => self.format(func(value))
  }

  object PrintableInstances {
    implicit val intPrintable = new Printable[Int] {
      override def format(value: Int): String = value.toString
    }

    implicit def listPrintable[A] = new Printable[List[A]] {
      override def format(value: List[A]): String = value.mkString(",")
    }

    implicit val booleanPrintable = new Printable[Boolean] {
      override def format(value: Boolean): String = if (value) "yes" else "no"
    }
  }

  def format[A](value: A)(implicit p: Printable[A]): String = p.format(value)

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
    import FunctorInstances._
    val func = LearningFunctor.apply[Option]
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

  "Functor custom intance for future with implicit context" in {
    import scala.concurrent.ExecutionContext.Implicits.global
    implicit def futureFunctor(implicit ec: ExecutionContext) = new Functor[Future] {
      override def map[A, B](fa: Future[A])(f: (A) => B): Future[B] = fa.map(f)(ec)
    }

    val func = Functor.apply[Future]
    Await.result(func.map(Future(10))(_.toString), Duration.Inf) should be("10")
  }

  "Functor for binary tree" in {
    sealed trait Tree[+A]
    final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
    final case class Leaf[A](value: A) extends Tree[A]

    def branch[A](left: Tree[A], right: Tree[A]): Tree[A] =
      Branch(left, right)

    def leaf[A](value: A): Tree[A] = Leaf(value)

    implicit val treeFunctor = new Functor[Tree] {
      override def map[A, B](fa: Tree[A])(f: (A) => B): Tree[B] = fa match {
        case Leaf(a) => Leaf(f(a))
        case Branch(l, r) => Branch(map(l)(f), map(r)(f))
      }
    }

    val tree = branch(branch(leaf(10), leaf(1)), leaf(2))
    val sTree = treeFunctor.map(tree)(_.toString)

    sTree should be(
      branch(
        branch(
          leaf("10"),
          leaf("1")),
        leaf("2")
      )
    )

    tree.map(_ * 2) should be(branch(branch(leaf(20), leaf(2)), leaf(4)))
  }

  "Contramap for printable" in {
    import PrintableInstances._

    implicit def otherPrintable[A] = listPrintable.contramap[A](List(_))

    implicit val intStringPrintable = intPrintable.contramap[String](_.toInt)

    format("10") should be("10")
    format(10) should be("10")
    format(true) should be("yes")
  }

  "Printable for box" in {
    import PrintableInstances._
    case class Box[A](value: A)

    implicit def boxPrintable[A](implicit ap: Printable[A]) = new Printable[Box[A]] {
      override def format(value: Box[A]): String = s"Box(${ap.format(value.value)})"
    }

    format(Box(true)) should be("Box(yes)")
    format(Box(10)) should be("Box(10)")
    format(Box(List(1, 2, 3))) should be("Box(1,2,3)")

    implicit val intBoxPrintable = boxPrintable[Int].contramap[String](s => Box(s.toInt))
    format("10") should be("Box(10)")
  }

  "Invariant functors codec functor" in {
    trait Codec[A] {
      def encode(value: A): String

      def decode(value: String): Option[A]

      def imap[B](dec: A => B, enc: B => A): Codec[B] = {
        val self = this
        new Codec[B] {
          override def encode(value: B): String = self.encode(enc(value))

          override def decode(value: String): Option[B] = self.decode(value).map(dec)
        }
      }
    }

    def encode[A](value: A)(implicit c: Codec[A]): String = c.encode(value)

    def decode[A](value: String)(implicit c: Codec[A]): Option[A] = c.decode(value)

    implicit val intCodec = new Codec[Int] {
      override def encode(value: Int): String = value.toString

      override def decode(value: String): Option[Int] = Try(value.toInt).map(i => Some(i)).getOrElse(None)
    }

    encode(10) should be("10")
    decode("10") should be(Some(10))

    implicit val intListCodec = intCodec.imap[List[Int]](dec = List(_), enc = _.head)

    encode(List(10, 20)) should be("10")
    decode("10")(intListCodec) should be (Some(List(10)))
  }

}
