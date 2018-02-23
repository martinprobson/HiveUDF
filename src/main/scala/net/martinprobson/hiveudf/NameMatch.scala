
package net.martinprobson.hiveudf

import grizzled.slf4j.Logging

/**
  * This enumeration contains possible results of the match
  *
  *  - EXACT Exact match on names.
  *  - SURNAME_FIRST_INITIAL  Surname and first initial match.
  *  - SURNAME Surname match.
  *  - COMPANY_NAME Company Name
  *  - EXECUTOR Executor account
  *  - JOINT_ACCOUNT
  *  - NONE No match found.
  *
  */
object MatchResult extends Enumeration {
   type MatchResult = Value
   
   val EXACT,
       SURNAME_FIRST_INITIAL,
       SURNAME,
       COMPANY_NAME,
       EXECUTOR,
       JOINT_ACCOUNT,
       NONE
       = Value
}
  

/**
  * This object implements simplistic name/surname matching between two supplied strings.
  */
object NameMatcher extends Logging {
 
 
  import MatchResult._
  
  /**
    * Compose our list of objects (that implement MatchTrait trait) into a call table.
    * These will get called in sequence.
    * 
    */
  //TODO This should probably be configurable externally.
  private def  fnTable: List[MatchTrait] = List(
                                                RemovePunct,
                                                Trim,
                                                Upper,
                                                NoMatchBlanks,
                                                FindExecutor,
                                                FindLTD,
                                                Exact,
                                                StripTitle,
                                                FindJoint,
                                                SurnameFirstInitial,
                                                Surname,
                                                ReverseFirstString,
                                                Exact,
                                                SurnameFirstInitial,
                                                Surname,
                                                NoMatch)

  /**
    * Execute the match process by calling each match function in sequence.
    * The idea is that the match will continue to call the chained match functions
    * (passing the results along the chain) until a MatchResult is obtained.
    *
    * ReturnType from each match function is an Either of MatchResult or a tuple2 of the (possibly) transformed
    * strings from the function that will be passed to the next.
    *
    * We are folding from the left on a Either[MatchResult,(String,String)] object,
    * 
    * e.g.
    * {{{
    * Right("Fred Smith "," Fred Smith") --> 
    *                               Trim ->  Right("Fred Smith","Fred Smith") ->
    *                               Upper -> Right("FRED SMITH","FRED SMITH") ->
    *                               Exact -> Left(MatchResult.EXACT)
    * }}}
    *
    * @param s1  The first name to be matched.
    * @param s2  The second names to be matched.
    *
    * @return The result of the match [[MatchResult]].
    */
  def executeMatch(s1: String, s2: String): MatchResult =
  	fnTable.foldLeft(Right((s1,s2)): Either[MatchResult,(String,String)]){ 
      (b,a) => b.right.flatMap { aa => a.exec(aa._1,aa._2) } }.left.get
	      
}  
  
