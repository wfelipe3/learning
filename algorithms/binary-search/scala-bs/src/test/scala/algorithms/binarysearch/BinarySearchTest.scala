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
    binarySearch(List(1), 1) should be(Some(0))
    binarySearch(List(2), 2) should be(Some(0))
  }

  "When list is size 2 and the value is not present then return None" in {
    binarySearch(List(1, 2), 3) should be(None)
  }

  "When list is size 2 and the value is present then return Some(index)" in {
    binarySearch(List(1, 2), 2) should be(Some(1))
    binarySearch(List(1, 2), 1) should be(Some(0))
  }

  def binarySearch(values: List[Int], value: Int): Option[Int] = {
    if (values.isEmpty) {
      None
    }
    else {
      val mid = values.size / 2
      if (values(mid) == value)
        Some(mid)
      else {
        val start = 0
        val end = (values.size / 2) - 1
        val mid = (end - start) / 2
        if (values(mid) == value)
          Some(mid)
        else
          None
      }
    }
  }

}
