object BreakExample {
  import util.control.Breaks._
  def main(args: Array[String]): Unit = {
    var list = List[Int]()
    breakable {
      for (i <- 1 to 10) {
        list ::= i
        if (i > 4) break // break out of the for loop
      }
    }
    println(list)
  }

}