package org.specs2.mutable
import org.specs2.matcher._
import org.specs2.specification._
import org.specs2.execute.StandardResults._
import ThrownExpectationsSpecData._
import org.specs2.execute.{DecoratedResult, Failure}

class ThrownExpectationsSpec extends Specification with ResultMatchers {
  implicit val arguments = args()

  "An acceptance spec using" >> {
    "ThrownExpectations should fail when the first must matcher fails in an Example" in {
      FragmentExecution.executeBody(body1) must beFailing
    }
    "ThrownExpectations should fail when the first should matcher fails in an Example" in {
      FragmentExecution.executeBody(body2) must beFailing
    }
    "MustThrownExpectations should fail when the first matcher fails in an Example" in {
      FragmentExecution.executeBody(body3) must beFailing
    }
    "ShouldThrownExpectations should fail when the first matcher fails in an Example" in {
      FragmentExecution.executeBody(body4) must beFailing
    }
  }
  "If a DataTable fails it must throw a DecoratedResultException containing the table data" in {
    FragmentExecution.executeBody(body5) must beLike { case DecoratedResult(_, Failure(_,_,_,_)) => ok }
  }
}
object ThrownExpectationsSpecData {
  def body1 = new MustExpectations with ThrownExpectations with Scope {
    1 must_== 2; success
  }
  def body2 = new ShouldExpectations with ThrownExpectations with Scope {
    1 should_== 2; success
  }
  def body3 = new MustThrownExpectations with Scope {
    1 must_== 2; success
  }
  def body4 = new ShouldThrownExpectations with Scope {
    1 should_== 2; success
  }
  def body5 = new MustMatchers with ThrownExpectations with Scope with DataTables {
    "a" | "b" | "c" |>
    1   ! 1   ! 2   |
    1   ! 1   ! 3   | { (a, b, c) => (a+b) must_== c }
  }
}