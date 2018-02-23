
package net.martinprobson.hiveudf

import org.apache.hadoop.hive.ql.exec.UDF

/** Matcher implements a Hive UDF.
  *
  * The Matcher is passed two strings on which the match is to be run.
  * The UDF will call a series of functions to transform and attempt to
  * match the strings.
  *
  * @see [[NameMatcher]]
  *
  */
class Matcher extends UDF {

  /**
    * @param name1  The first name to be matched.
    * @param name2  The second names to be matched.
    *
    * @return The result of the match (as a String).
    *
    * Example:
    *
    * {{{
    *   executeMatch("Miss Anne Smith","Miss Anne Smith")
    * }}}
    * returns "EXACT"
    *
    * {{{
    *   executeMatch("SMITH K","Mrs Kay Smith")
    * }}}
    * returns "SURNAME"
    *
    */
  def evaluate(name1: String, name2: String) = 
    NameMatcher.executeMatch(name1, name2).toString
}


