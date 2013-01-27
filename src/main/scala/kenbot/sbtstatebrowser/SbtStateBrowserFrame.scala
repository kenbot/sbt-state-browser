package kenbot.sbtstatebrowser
import scala.swing._
import Swing.pair2Dimension
import scalaswingcontrib.tree._
import sbt._
import xsbti.AppConfiguration

class SbtStateBrowserFrame(state: State) extends Frame {
  title = "sbt State Browser"

  val stateBrowserTree = new SbtStateBrowserTree(state)

  contents = searchableTree
  
  private def searchableTree = new SearchableTree(stateBrowserTree, 
      stateBrowserTree.createModel, 
      stateBrowserTree.getChildNodes)
  
  override def closeOperation() = dispose()
  
  pack()
  def show() { visible = true }
}


