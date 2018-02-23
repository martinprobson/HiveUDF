package net.martinprobson.hiveudf

import MatchResult.MatchResult
import org.scalatest.FunSuite

class TestFindJoint extends FunSuite {

  case class TestCond(tc: Int, s1: String, s2: String, expectedResult: Either[MatchResult, (String, String)])

  val testConds = List(TestCond(1, "Mr Fred Bloggs", "Fred Bloggs", Right(("Mr Fred Bloggs", "Fred Bloggs"))),
    TestCond(2, "Fred Mr Bloggs", "Fred Mr Bloggs", Right(("Fred Mr Bloggs", "Fred Mr Bloggs"))),
    TestCond(3, "MR C SMith", "Mr & Mrs Chris SMith", Left(MatchResult.JOINT_ACCOUNT)),
    TestCond(4, "J & K M Hill", "Mr Gary HILL", Left(MatchResult.JOINT_ACCOUNT)),
    TestCond(5, "D & C TEN", "Mr David M ten", Left(MatchResult.JOINT_ACCOUNT)),
    TestCond(6, "D W & P V SH", "Mrs Patr Sh", Left(MatchResult.JOINT_ACCOUNT)))


  testConds.foreach { case TestCond(n, s1, s2, exp) => {
    test(s"TestFindJoint: Test Case $n") {
      assert(FindJoint.matchFunc(s1, s2) === exp)
    }
  }

  }
}
