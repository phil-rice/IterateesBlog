import play.api.libs.iteratee._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.Promise

object EnumerateeExample {
  def main(args: Array[String]): Unit = {
    val sumIteratee = Iteratee.fold[Int, Int](0) { (total, elt) => total + elt }
    val e1 = Enumerator("1", "234", "455", "987")
    val f: Future[Int] = e1.through(Enumeratee.map(_.toInt)).run(sumIteratee)
    f.onSuccess { case x: Int => println(s"run produced $x") }
    println("Done")
    Thread.sleep(1000)
    println("Really Done")
  }
}