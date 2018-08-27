package com.learning

object Sets extends App {

  val value = new NonEmpty(4, Empty, Empty)
  val value2 = new NonEmpty(5, Empty, Empty)
  val v = new NonEmpty(4, new NonEmpty(3, Empty, Empty), new NonEmpty(5, Empty, Empty))
  private val set: IntSet = v.incl(8)

  println(Empty)
  println(value)
  println(v)
  println(set)
  println(value union value2)
  println(v union set)
  println(
    new NonEmpty(
      5,
      new NonEmpty(4,
        new NonEmpty(3, Empty, Empty),
        Empty),
      new NonEmpty(7, Empty, Empty)
    ).union(
      new NonEmpty(9,
        new NonEmpty(8, Empty, Empty),
        new NonEmpty(10, Empty, Empty)
      )
    )
  )
}

trait IntSet {
  def contains(x: Int): Boolean

  def incl(x: Int): IntSet

  def union(other: IntSet): IntSet
}

object Empty extends IntSet {
  override def contains(x: Int): Boolean = false

  override def incl(x: Int): IntSet = new NonEmpty(x, Empty, Empty)

  override def union(other: IntSet): IntSet = other

  override def toString: String = """"$E""""
}

class NonEmpty(value: Int, left: IntSet, right: IntSet) extends IntSet {

  override def contains(x: Int): Boolean =
    if (x > value) right contains x
    else if (x < value) left contains x
    else true

  override def incl(x: Int): IntSet =
    if (x > value) new NonEmpty(value, left, right incl x)
    else if (x < value) new NonEmpty(value, left incl x, right)
    else this

  override def union(other: IntSet): IntSet =
    right union left union other incl value

  override def toString: String =
    s"""{"v": $value, "l": ${left.toString}, "r": ${right.toString}}"""


}

