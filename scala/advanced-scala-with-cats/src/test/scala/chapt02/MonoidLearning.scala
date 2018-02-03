package chapt02

import org.scalatest.{FreeSpec, Matchers}
import cats.Monoid
import cats.Semigroup
import cats.instances.string._
import cats.instances.option._
import cats.instances.int._
import cats.syntax.semigroup._

/**
  * Created by feliperojas on 2/3/18.
  */
class MonoidLearning extends FreeSpec with Matchers {

  trait LearnSemigroup[A] {
    def combine(x: A, y: A): A
  }

  trait LearnMonoid[A] extends LearnSemigroup[A] {
    def empty: A
  }

  object LearnMonoid {
    def apply[A](implicit monoid: LearnMonoid[A]) = monoid
  }

  object MonoidInstnaces {
    implicit val intSumMonoid = new LearnMonoid[Int] {
      override def combine(x: Int, y: Int): Int = x + y

      override def empty: Int = 0
    }

    implicit val booleanAndMonoid = new LearnMonoid[Boolean] {
      def combine(x: Boolean, y: Boolean): Boolean = x && y

      def empty: Boolean = true
    }

    implicit val booleanOrMonoid = new LearnMonoid[Boolean] {
      def combine(x: Boolean, y: Boolean): Boolean = x || y

      def empty: Boolean = false
    }

    implicit def setMonoid[A] = new LearnMonoid[Set[A]] {
      override def empty: Set[A] = Set.empty

      override def combine(x: Set[A], y: Set[A]): Set[A] = x ++ y
    }
  }

  def associativeLaw[A](x: A, y: A, z: A)(implicit m: LearnMonoid[A]) = {
    m.combine(x, m.combine(y, z)) should be(m.combine(m.combine(x, y), z))
  }

  def identityLaw[A](x: A)(implicit m: LearnMonoid[A]) = {
    (m.combine(x, m.empty) == x && m.combine(m.empty, x) == x) should be(true)
  }

  "Monoid laws" in {
    import MonoidInstnaces._
    associativeLaw(10, 20, 30)
    identityLaw(10)
    associativeLaw(true, false, true)(booleanOrMonoid)
    identityLaw(true)(booleanOrMonoid)
    associativeLaw(true, false, true)(booleanAndMonoid)
    identityLaw(true)(booleanAndMonoid)
  }

  "Set monoid" in {
    import MonoidInstnaces._
    val monoid = LearnMonoid.apply[Set[Int]]
    monoid.combine(Set(1, 2, 3), Set(4, 5, 6)) should be(Set(1, 2, 3, 4, 5, 6))

    def intersectionSemigroup[A] = new LearnSemigroup[Set[A]] {
      override def combine(x: Set[A], y: Set[A]): Set[A] = x intersect y
    }

    intersectionSemigroup.combine(Set(1, 2, 3), Set(2, 3, 4)) should be(Set(2, 3))
  }

  "Cats monoid" in {
    val stringMonoid = Monoid.apply[String]
    val stringSemigroup = Semigroup.apply[String]
    stringMonoid.combineAll(List("this", "is", "a", "test")) should be("thisisatest")
    stringSemigroup.combineAllOption(List.empty[String]) should be(None)
    val optionMonoid = Monoid[Option[Int]]
    optionMonoid.combine(Some(10), Some(20)) should be(Some(30))
    "Hi" |+| "there" |+| Monoid[String].empty should be("Hithere")
    1 |+| 2 should be(3)
  }

  "implement add items with monoid" in {
    def add[A: Monoid](items: List[A]): A = {
      val monoid = implicitly[Monoid[A]]
      items.foldRight(monoid.empty)(monoid.combine)
    }

    add(List(1, 2, 3)) should be(1 + 2 + 3)
    add(List[Option[Int]](Some(1), Some(2), Some(3))) should be(Some(1 + 2 + 3))
  }
}
