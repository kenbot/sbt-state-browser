#sbt-state-browser

This is an sbt 0.12 plugin, which will give your project a <code>browse-sbt-state</code> command.  This will launch a GUI window allowing you to explore the <code>sbt.State</code> object graph that represents your build.   Somewhat-functioning search bar included!

I haven't put the binaries anywhere useful, so for the time being, you can build it from source by putting this in a .scala file in your <code>$PROJECT_BASE/project/project</code> directory:
  
<code>
```scala
import sbt._
object PluginDef extends Build {
   override def projects = Seq(root dependsOn sbtStateBrowserPlugin)
   def root = Project("plugins", file(".")) 
   def sbtStateBrowserPlugin = uri("git://github.com/kenbot/sbt-state-browser.git")
}
```
</code>

I'm told this kind of thing gets a bit easier to do in sbt 0.13.