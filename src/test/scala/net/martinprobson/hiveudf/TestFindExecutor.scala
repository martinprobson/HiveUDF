package net.martinprobson.hiveudf

import MatchResult.MatchResult
import org.scalatest.FunSuite

class TestFindExecutor extends FunSuite {

  case class TestCond(tc: Int, s1: String, s2: String, expectedResult: Either[MatchResult, (String, String)])

  val testConds = List(TestCond(1, "The Executor Of   Mr F L SMTH", "Executors of SMTH", Left(MatchResult.EXECUTOR)),
    TestCond(2, "Fred Mr Bloggs", "Fred Mr Bloggs", Right(("FRED MR BLOGGS", "FRED MR BLOGGS"))),
    TestCond(3, "Executor of Mr S Hole Deceased", "Mrs Dsda", Left(MatchResult.EXECUTOR)),
    TestCond(4, "Mrs S Bloggs", "The Estate of Mrs Bloggs", Left(MatchResult.EXECUTOR)))


  testConds.foreach { case TestCond(n, s1, s2, exp) => {
    test(s"TestFindExecutor: Test Case $n") {
      assert(FindExecutor.matchFunc(s1.trim.toUpperCase, s2.trim.toUpperCase) === exp)
    }
  }

  }
}
