package org.squeryl.dbagnostic

import _root_.org.specs.Specification
import _root_.org.squeryl.adapters.OracleAdapter
import org.specs._
import scala.collection.mutable.HashSet


class OracleAdapterTests extends Specification {

  val oa = new OracleAdapter

  classOf[OracleAdapter].getName should {

    val possibilities = oa.paddingPossibilities("xxx", 3)

    "have method 'paddingPossibilities' generate unique sequences " in {
      val set = possibilities.toSet
      possibilities must haveSize(set.size)
    }

    "have method 'paddingPossibilities' generate strings with prescribed length " in {
      possibilities.filter(_.length != 6) must beEqualTo(Nil)
    }

    "make String identifier unique in a given Set, by padding right side" in {

      val scope = new HashSet[String]

      val setOfPossibleUniqeIdsWithAB = oa.legalOracleSuffixChars.size * 2 + 1

      for(i <- 1 to setOfPossibleUniqeIdsWithAB) {
        val id = oa.makeUniqueInScope("AB", scope)
        scope.add(id)
      }

      scope must haveSize(setOfPossibleUniqeIdsWithAB)
    }

    "shrink identifiers and preserve uniqueness in a given Set" in {
      shrinkTestSet must haveSize(50)
    }

    "shrink identifiers to a length of 30" in {
      shrinkTestSet.filter(_.length != 30) must haveSize(0)
    }
  }  

  def shrinkTestSet = {
    val scope = new HashSet[String]
    for(i <- 1 to 50)
      oa.shrinkTo30AndPreserveUniquenessInScope("UYTFBTFBTFNIUNGONGONYGNOYNGOYGN&%DTFKTFIO&%DTFKTFIO", scope)
    scope
  }
}








