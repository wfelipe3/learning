package recfun

import scala.annotation.tailrec

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
  }

  /**
    * Exercise 1
    */
  def pascal(c: Int, r: Int): Int =
    if (c == 0 || c == r) 1
    else pascal(c - 1, r - 1) + pascal(c, r - 1)

  /**
    * Exercise 2
    */
  def balance(chars: List[Char]): Boolean = {
    @tailrec
    def balance(acc: Int, chars: List[Char]): Boolean =
      if (chars.isEmpty)
        acc == 0
      else if (acc < 0)
        false
      else if (chars.head == '(')
        balance(acc + 1, chars.tail)
      else if (chars.head == ')')
        balance(acc - 1, chars.tail)
      else
        balance(acc, chars.tail)

    balance(0, chars)
  }

  /**
    * Exercise 3
    */
  def countChange(money: Int, coins: List[Int]): Int = {
    def loop(amount: Int, coins: List[Int]): Int = {
      if (coins.isEmpty) 0
      else if (amount == money) 1
      else if (amount > money) 0
      else loop(amount + coins.head, coins) + loop(amount + coins.head, coins.tail)
    }

    loop(0, coins)
  }
}
