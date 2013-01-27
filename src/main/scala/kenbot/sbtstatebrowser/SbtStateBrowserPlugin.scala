package kenbot.sbtstatebrowser
import sbt._
import Keys._

object SbtStateBrowserPlugin extends Plugin {

  val browseSbtState = Command.command("browse-sbt-state") { state =>   
    new SbtStateBrowserFrame(state).show()
    state
  }
  
  override lazy val settings = Seq(
    commands += browseSbtState
  )
}
