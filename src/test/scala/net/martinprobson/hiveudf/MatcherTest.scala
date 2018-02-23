
package net.martinprobson.hiveudf

import scala.util.{Failure, Success, Try}
import scala.io.Source.fromInputStream
import org.scalatest.FunSuite



class MatcherTest extends FunSuite {

  case class MatcherTCase(tc: Int, name1: String, name2: String, expResult: String)

  def getResource(resource: String): List[String] =
    Try(fromInputStream(getClass.getResourceAsStream(resource)).getLines.toList) match {
      case Success(lines) => lines
      case Failure(ex) => throw new Exception(s"Error reading test case file: $resource ", ex)
    }


  def getTestConditions(file: String): List[MatcherTCase] =
    getResource("/testCases.txt") map (_.split("\\|", -1)) map (col => MatcherTCase(col(0).toInt, col(1), col(2), col(3)))


  getTestConditions("\testCases.txt").foreach { case MatcherTCase(tc, n1, n2, expResult) => {
    test(s"TC: $tc - Input: name 1: $n1 name 2: $n2 Expected: $expResult") {
      assert(NameMatcher.executeMatch(n1, n2).toString === expResult)
    }
  }
  }
}
