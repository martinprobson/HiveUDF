
package net.martinprobson.hiveudf

import MatchResult._

/**
  * Remove punctuation
  */
object RemovePunct extends MatchTrait {

  override def matchFunc(s1: String, s2: String) =
    Right((s1.replaceAll("[\\-.,_+$'%!():;]",""),s2.replaceAll("[\\-.,_+$'%!():;]","")))
}


/**
  * Clean up leading and trailing spaces
  */
object Trim extends MatchTrait {

  override def matchFunc(s1: String, s2: String) = Right((s1.trim,s2.trim))
}


/**
  * Convert both strings to upper case
  */
object Upper extends MatchTrait {
  override def matchFunc(s1: String, s2: String) = Right((s1.toUpperCase,s2.toUpperCase))
}


/**
  * Strip title from beginning of String.
  */
object StripTitle extends MatchTrait {

  override def matchFunc(s1:String, s2:String) = {

    /**
      * Attempt to remove a title from beginning of passed String t.
      * @param t - The target String
      * @return - The target String with the title removed or target string if not found.
      */
    def removeFirstTitle(t: String): String = {
      import scala.util.matching.Regex
      lazy val regExps = Stream("MR","MRS","MISS","MS","DR").map { (s) => new Regex(s"^ *$s +") }

      regExps.map { _.replaceFirstIn(t,"")}.dropWhile( _ == t).headOption.getOrElse(t)
    }

    Right((removeFirstTitle(s1),removeFirstTitle(s2)))
  }
}


  
/**
  * Surname and first initial
  */
object SurnameFirstInitial extends MatchTrait {

  def surname(s: String) = s.reverse.split(" ")(0).reverse

  override def matchFunc(s1:String, s2:String) = {
    def firstInitial(s: String) = s.split(" ")(0)(0)
    val (a,b,c,d) = (surname(s1), surname(s2), firstInitial(s1), firstInitial(s2))
    if ((s1.split(" ").length != 1 && s2.split(" ").length != 1) &&
       (surname(s1) == surname(s2) && firstInitial(s1) == firstInitial(s2)))
      Left(SURNAME_FIRST_INITIAL)
    else
      Right((s1,s2))
  }
}

/**
  * Surname only
  */
object Surname extends MatchTrait {

  def surname(s: String) = s.reverse.split(" ")(0).reverse

  override def matchFunc(s1:String, s2:String) = 
    if (surname(s1) == surname(s2)) Left(SURNAME) else Right((s1,s2))
}
  

/**
  * Check id the two strings are equal, return EXACT if they are.
  * Otherwise pass the Strings along the chain unchanged.
  */
object Exact extends MatchTrait {
 override def matchFunc(s1: String, s2: String) =
   if (s1 == s2) Left(EXACT) else Right((s1,s2))
}

/**
  * If either string is blank - no match
  */
object NoMatchBlanks extends MatchTrait {
 override def matchFunc(s1: String, s2: String) = (s1,s2) match {
    case ("",_) => Left(NONE)
    case (_,"") => Left(NONE)
    case (a,b)  => Right((a,b))
  }
}

/**
  * Reverse the first string
  */
object ReverseFirstString extends MatchTrait {
  override def matchFunc(s1: String, s2: String) =
     Right( ((s1.reverse.split(" ", -1) map(_.reverse)).mkString(" "),s2))
}

/**
  * Ltd company in either string.
  */
object FindLTD extends MatchTrait {
  override def matchFunc(s1: String, s2: String) = {
    val regx = """.*?\bLTD\b.*?""".r
    if (regx.findFirstIn(s1).isDefined || regx.findFirstIn(s2).isDefined )
      Left(COMPANY_NAME)
    else
      Right((s1,s2))
  }
}

/**
  * Executor or 'The Estate of' in either string.
  */
object FindExecutor extends MatchTrait {
  override def matchFunc(s1: String, s2: String) = {
    val regx = """.*?\bEXECU.*?|.*?\bESTATE OF\b.*?""".r
    if (regx.findFirstIn(s1).isDefined || regx.findFirstIn(s2).isDefined )
      Left(EXECUTOR)
    else
      Right((s1,s2))
  }
}

/**
  * Look for joint accounts
  */
object FindJoint extends MatchTrait {
  override def matchFunc(s1: String, s2: String) = {
    val regx = """.*?&.*?|.*?\bAND\b.*?""".r
    if (regx.findFirstIn(s1).isDefined || regx.findFirstIn(s2).isDefined )
      Left(JOINT_ACCOUNT)
    else
      Right((s1,s2))
  }
}


/**
  * No match can be found - should be at the end of the chain
  */
object NoMatch extends MatchTrait {
  override def matchFunc(s1: String, s2: String) = Left(NONE)
}


