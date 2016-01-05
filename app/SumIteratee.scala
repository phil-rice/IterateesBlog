import play.api.libs.iteratee._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object SumIterateeExample {

  def sumIteratee: Iteratee[Int, Int] = {
    def step(total: Int)(i: Input[Int]): Iteratee[Int, Int] = i match {
      case Input.EOF | Input.Empty => Done(total, Input.EOF)
      case Input.El(e)             => Cont[Int, Int](i => step(total + e)(i))
    }
    Cont[Int, Int](i => step(0)(i))
  }
  def exitEarlyIteratee(maxValue: Int): Iteratee[Int, Int] = {
    def step(total: Int)(i: Input[Int]): Iteratee[Int, Int] = i match {
      case Input.EOF | Input.Empty => Done(total, Input.EOF)
      case Input.El(e) =>
        if (e <= maxValue)
          Cont[Int, Int](i => step(total + e)(i))
        else
          Done(total, Input.EOF)
    }
    Cont[Int, Int](i => step(0)(i))
  }
  def nPlusOne(finish: Int => Boolean)(fn: (Int, Int) => Int): Iteratee[Int, Int] = {
    def step(acc: Int)(i: Input[Int]): Iteratee[Int, Int] = i match {
      case Input.EOF | Input.Empty => Done(acc, Input.EOF)
      case Input.El(e) => if (finish(e))
        Done(acc, Input.EOF)
      else
        Cont[Int, Int](i => step(fn(acc, e))(i))
    }
    Cont[Int, Int](i => step(0)(i))
  }

  def main(args: Array[String]): Unit = {
    val e1 = Enumerator(1, 234, 455, 987)
    //    val f: Future[Int] = e1.run(exitEarlyIteratee(300))
    val nPlusOneIterator = nPlusOne(_ > 300) { _ + _ }
    val f: Future[Int] = e1.run(nPlusOneIterator).map(total => total + 1)
    f.onSuccess { case x: Int => println(s"run produced $x") }
    println("Done")
    Thread.sleep(1000)
    println("Really Done")
  }
}