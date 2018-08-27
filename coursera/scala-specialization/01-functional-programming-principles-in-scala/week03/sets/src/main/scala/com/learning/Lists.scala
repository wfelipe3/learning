package com.learning

object Lists {

  def nth[T](index: Int, l: List[T]): T =
    if (l.isEmpty) throw new IndexOutOfBoundsException
    else if (index == 0) l.head
    else nth(index - 1, l.tail)
}

trait List[T] {
  def head: T

  def tail: List[T]

  def isEmpty: Boolean
}

object Nil extends List[Nothing] {
  override def head: Nothing = throw new NoSuchElementException("Nil.head")

  override def tail: List[Nothing] = throw new NoSuchElementException("Nil.tail")

  override def isEmpty: Boolean = true
}

class Cons[T](val head: T, val tail: List[T]) extends List[T] {
  override def isEmpty: Boolean = false
}

