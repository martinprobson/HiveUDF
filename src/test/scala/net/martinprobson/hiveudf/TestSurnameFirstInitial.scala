package net.martinprobson.hiveudf

import MatchResult.MatchResult
import org.scalatest.FunSuite

class TestSurnameFirstInitial extends FunSuite {

  case class TestCond(tc: Int, s1: String, s2: String, expectedResult: Either[MatchResult, (String, String)])

  val testConds = List(TestCond(1, "KATHLEEN BIVENS","KATHLEEN BIVENS", Left(MatchResult.SURNAME_FIRST_INITIAL)),
                       TestCond(2,"KESSLER","KATHARINE KESSLER",Right("KESSLER","KATHARINE KESSLER")))


  testConds.foreach { case TestCond(n, s1, s2, exp) => {
    test(s"TestSurnameFirstInitial: Test Case $n") {
      assert(SurnameFirstInitial.matchFunc(s1, s2) === exp)
    }
  }

  }
}
