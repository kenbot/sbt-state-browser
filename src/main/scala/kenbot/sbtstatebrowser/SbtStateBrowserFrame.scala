package kenbot.sbtstatebrowser
import scala.swing._
import Swing.pair2Dimension
import scalaswingcontrib.tree._
import sbt._
import xsbti.AppConfiguration

class SbtStateBrowserFrame(state: State) extends Frame {
  title = "sbt State Browser"

  val stateBrowserTree = new SbtStateBrowserTree(state, state)
  
  val extractedTree = {
    val Extracted(structure, settings, projectRef) = Project extract state
    new SbtStateBrowserTree(state, structure, settings, projectRef)
  }
  
  contents = new TabbedPane {
    import TabbedPane.Page
    pages += new Page("sbt.State", makeSearchableTree(stateBrowserTree))
    pages += new Page("Project.extract(state)", makeSearchableTree(extractedTree))
  }
  
  private def makeSearchableTree(browserTree: SbtStateBrowserTree) = 
      new SearchableTree(browserTree, browserTree.createModel, browserTree.getChildNodes)

  override def closeOperation() = dispose()
  
  pack()
  def show() { visible = true }
}


