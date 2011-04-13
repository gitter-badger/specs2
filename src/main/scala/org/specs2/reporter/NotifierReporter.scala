package org.specs2
package reporter

import execute._
import specification._
import Levels._
import specification.SpecificationStructure
import scalaz.Tree
import main.Arguments

/**
 * Report a Specification by notifying execution events to a Notifier
 */
trait NotifierReporter extends Reporter
    with DefaultSelection
    with DefaultSequence
    with DefaultExecutionStrategy
    with NotifierExporting

trait NotifierExporting extends Exporting {
  type ExportType = Unit
  val notifier: Notifier
  /** @return a function exporting ExecutedFragments */
  def export(s: SpecificationStructure)(implicit args: Arguments): Seq[ExecutedFragment] => ExportType = (fs: Seq[ExecutedFragment]) => {
    export(Levels.foldAll(fs).toTree(mapper))
    ()
  }
  private val mapper = (f: ExecutedFragment, i: Int) => f match {
    case e: ExecutedStandardFragment => None
    case other                       => Some(other)
  }
  private def export(tree: Tree[ExecutedFragment]) {
    tree.rootLabel match {
      case f @ ExecutedSpecStart(n, _, _)                       => {
        notifier.specStart(n.name, f.location.toString)
        tree.subForest.foreach(export)
      }
      case f @ ExecutedSpecEnd(n, _)                            => {
        notifier.specEnd(n.name, f.location.toString)
      }
      case f @ ExecutedText(t, _)  if (tree.subForest.isEmpty)  => notifier.text(t, f.location.toString)
      case f @ ExecutedText(t, _)                               => {
        notifier.contextStart(t, f.location.toString)
        tree.subForest.foreach(export)
        notifier.contextEnd(t, f.location.toString)
      }
      case f @ ExecutedResult(s, r, t, l)  => {
        notifier.exampleStarted(s.toString, l.toString)
        r match {
          case Success(_)              => notifier.exampleSuccess(r.message, t.stop.elapsed)
          case fail @ Failure(_,_,_,_) => notifier.exampleFailure(r.message, fail.exception, t.stop.elapsed)
          case err  @ Error(_,_)       => notifier.exampleError(r.message, err.exception, t.stop.elapsed)
          case Skipped(_,_)            => notifier.exampleSkipped(r.message, t.stop.elapsed)
          case Pending(_)              => notifier.examplePending(r.message, t.stop.elapsed)
        }
      }
      case other                           => tree.subForest.foreach(export)
    }
  }
}

/**
 * This trait can be used for any event concerning the execution of examples
 * seen as a Tree of Fragments.
 *
 * A pair of contextStart/contextEnd calls delimits a sequence of children in that tree.
 */
trait Notifier {
  def specStart(title: String, location: String)
  def specEnd(title: String, location: String)
  def contextStart(text: String, location: String)
  def contextEnd(text: String, location: String)
  def text(text: String, location: String)
  def exampleStarted(text: String, location: String)
  def exampleSuccess(text: String, duration: Long)
  def exampleFailure(message: String, f: Throwable, duration: Long)
  def exampleError(message: String, f: Throwable, duration: Long)
  def exampleSkipped(message: String, duration: Long)
  def examplePending(message: String, duration: Long)
}