package net.martinprobson.hiveudf

import org.scalatest.FunSuite

class TestStripTitle extends FunSuite {

  case class TestCond(tc: Int,s1: String, expectedResult: String)
  val testConds = List( TestCond(1 ,"Mr Fred Bloggs","Fred Bloggs"),
                        TestCond(2 ,"Dr Fred Bloggs","Fred Bloggs"),
                        TestCond(3 ," Miss Fred Bloggs","Fred Bloggs"),
                        TestCond(4 ,"Mrs Fred Bloggs","Fred Bloggs"),
                        TestCond(5 ," Ms Fred Bloggs","Fred Bloggs"),
                        TestCond(6 ,"DrFred Bloggs","DrFred Bloggs"),
                        TestCond(7 ,"Miss Miss Fred Bloggs","Miss Fred Bloggs"),
                        TestCond(8 ,"Mrs Mr Fred Bloggs","Mr Fred Bloggs"),
                        TestCond(9 ,"Fred Bloggs","Fred Bloggs"),
                        TestCond(10,"Fred Mr Bloggs","Fred Mr Bloggs"),
                        TestCond(11,"MISS KATHLEEN BIVENS","KATHLEEN BIVENS"),
                        TestCond(12,"",""))

  testConds.foreach{ case TestCond(n,s,exp) => {
    test(s"TestStripTitle: Test Case $n") {
      val t = s.toUpperCase.trim
      val e = exp.toUpperCase.trim
      assert( StripTitle.matchFunc(t,t) === Right((e,e)))}}}

}
