package algorithms.binarysearch

import org.scalatest.{FreeSpec, Matchers}

class BinarySearchTest extends FreeSpec with Matchers {

  "when list is empty then return none" in {
    binarySearch(List.empty[Int], 3) should be(None)
  }

  "When list does no have the value then return None" in {
    binarySearch(List(1), 3) should be(None)
  }

  "When List is size 1 and the value is present, then return Some(index)" in {
    binarySearch(List(1), 1) should be(Some(1))
  }

  def binarySearch(values: List[Int], value: Int): Option[Int] = {
    if (values.isEmpty)
      None
    else if (values.head == value)
      Some(1)
    else
      None
  }

}
