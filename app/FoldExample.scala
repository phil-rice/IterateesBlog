object FoldExample {
  import util.control.Breaks._
  def main(args: Array[String]): Unit = {
    val result = (1 to 10).foldLeft((List[Int](), true)) {
      case (result @ (acc, cont), i) =>
        if (cont) (i :: acc, i <= 4) else result
    }
    println(result._1)
  }
}