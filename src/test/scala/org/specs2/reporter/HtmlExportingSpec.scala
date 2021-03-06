package org.specs2
package reporter
import specification._
import main._
import ExecutedSpecificationData._

class HtmlExportingSpec extends Specification with ScalaCheck { def is = s2"""

The HtmlExporting trait is responsible for exporting the executed specification:

  ExecutedSpecification => Unit

While the formal type of output is Unit, the HtmlExporting trait actually transforms the ExecutingSpecification to a sequence of HtmlFiles to write to disk:


                         `print`               `writeFiles`
  `ExecutedSpecification   =>    Seq[HtmlFile]      =>       Unit`

  where `HtmlFile  = (Url, NodeSeq)`

  The number of created HtmlFiles must be 1 + number of linked specifications $e1
  The HtmlPrinter trait creates the ${"`HtmlFiles`" ~/ new HtmlPrinterSpec}
  The HtmlFileWriter trait writes the ${"`HtmlFiles` to disk" ~/ new HtmlFileWriterSpec}
                                                                                                                        """
  def exporter = new HtmlExporting {}
  
  def e1 = check { (spec: ExecutedSpecification) =>
    (1 + spec.includedLinkedSpecifications.size) === exporter.print(spec)(args()).size
  }
}


trait FunctionSpecification[A, B] extends Specification with Function[A, B] {
  def data: DataSpecificationStructure[A]
  def function: A => B
  def apply(a: A) = function(a)
}
trait Function2Specification[A, B, C] extends Specification with Function2[A, B, C] {
  def data: (DataSpecificationStructure[A], DataSpecificationStructure[B])
  def function: (A, B) => C
  def apply(a: A, b: B) = function(a, b)
}


class HtmlExporting0Spec extends Function2Specification[ExecutingSpecification, Arguments, Unit] {
  def data = (new ExecutedSpecificationSpec, new ArgumentsSpec)
  def function = { case (spec, args) => new HtmlExporting {}.export(args)(spec) }
  def is = success
}


class ExecutedSpecificationSpec extends DataSpecification[ExecutingSpecification] {
  def is = success
} 


class ArgumentsSpec extends DataSpecification[Arguments] {
  def is = success
}