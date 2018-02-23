package net.martinprobson.hiveudf

import grizzled.slf4j.Logging
import MatchResult._

/**
  * Trait that the match classes must implement, it contains an exec function that is called by the
  * matcher.
  *
  * @note The trait could be removed and the matcher could use pure (possibly anon) functions instead,
  *       but this method makes trace logging easier.
  */
trait MatchTrait extends Logging {

  /** Get the name of the match function (for logging purposes */
  private def getName = getClass.getName

  /**
    * Abstract match function.
    *
    * @param s1 - First string to be matched
    * @param s2 - Second string to be matched
    * @return MatchResult @see [[MatchResult]]
    */
  def matchFunc(s1: String, s2: String): Either[MatchResult,(String,String)]

  /**
    * Execute the match function, and log the name,input and output.
    *
    * @param s1 - First string to be matched
    * @param s2 - Second string to be matched
    * @return Either[MatchResult,(String,String)]
    */
  def exec(s1: String, s2: String): Either[MatchResult,(String,String)] = {
    val ret = matchFunc(s1,s2)
    trace(s"${getName}($s1,$s2) => ${ret.toString}")
    ret
  }
}
