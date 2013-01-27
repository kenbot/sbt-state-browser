package kenbot.sbtstatebrowser
import scala.swing._
import Swing.pair2Dimension
import scalaswingcontrib.tree._
import sbt._
import xsbti.AppConfiguration
import ScrollPane.BarPolicy._

class SearchableTree[A](tree: Tree[A], recreateModel: => TreeModel[A], getChildren: A => Seq[A]) extends BorderPanel {
  private val searchBar = new SearchBar
  
  val scrollPane = new ScrollPane(tree) {
    horizontalScrollBarPolicy = AsNeeded
    verticalScrollBarPolicy = AsNeeded
  }
  
  listenTo(searchBar)
  reactions += {
    case SearchEvent(_, searchString) =>  filterBy(searchString)
  }
  
  def filterBy(searchString: String) {
    def matches(a: A): Boolean = 
      (a.toString.toLowerCase contains searchString.toLowerCase) ||
        (getChildren(a) exists matches)
    
    Async.async(recreateModel filter matches) { filteredModel => 
      tree.model = filteredModel
      tree expandRow 0
      tree.revalidate()
      tree.repaint()
    }
  }
  
  import BorderPanel.Position._
  layout(searchBar) = North
  layout(scrollPane) = Center
}

object Async {
  def async[A](thunk: => A)(whenDone: A => Unit) {
    val swingWorker = new javax.swing.SwingWorker[A, Any] {
      override def doInBackground(): A = thunk
      override def done(): Unit = whenDone(get)
    }
    swingWorker.execute()
  }
}
