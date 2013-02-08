package kenbot.sbtstatebrowser
import scala.swing._
import Swing.pair2Dimension
import scalaswingcontrib.tree._
import sbt._
import xsbti.AppConfiguration

class SbtStateBrowserTree(state: State, roots: Any*) extends Tree[Any] {
  model = createModel
  renderer = Tree.Renderer(renderNode)
  
  def createModel = InternalTreeModel(roots: _*)(getChildNodes)
  
  def getChildNodes(a: Any): Seq[Any] = a match {
    case (_, value) => getChildNodes(value)
    case s: State => Seq(
      "configuration" -> s.configuration,
      "definedCommands" -> s.definedCommands, 
      "exitHooks" -> s.exitHooks,
      "onFailure" -> s.onFailure,
      "remainingCommands" -> s.remainingCommands, 
      "history" -> s.history,
      "attributes" -> s.attributes,
      "globalLogging" -> s.globalLogging,
      "next" -> s.next
    )
    case h: State.History => Seq(
      "current" -> h.current,
      "executed" -> h.executed,
      "maxSize" -> h.maxSize,
      "previous" -> h.previous
    )
    case a: AttributeMap => a.entries.toSeq
    case AttributeEntry(key, value) => Seq("key" -> key, "value" -> value)
    case k: AttributeKey[_] => Seq(
      "description" -> k.description, 
      "extend" -> k.extend, 
      "isLocal" -> k.isLocal, 
      "manifest" -> k.manifest, 
      "rank" -> k.rank)
    case c: AppConfiguration => Seq(
      "arguments" -> c.arguments, 
      "baseDirectory" -> c.baseDirectory)
    case c: Command => Seq(
      "help" -> c.help(state), 
      "tags" -> c.tags)
    case h: Help => Seq(
      "brief" -> h.brief, 
      "detail" -> h.detail, 
      "more" -> h.more)
    case s: SessionSettings => Seq(
      "currentBuild" -> s.currentBuild,
      "currentProject" -> s.currentProject,
      "original" -> s.original,
      "append" -> s.append,
      "rawAppend" -> s.rawAppend,
      "currentEval" -> s.currentEval
    )
    case s: Settings[_] => Seq(
      "data" -> s.data,
      "scopes" -> s.scopes
    )
    case b: Load.BuildStructure => Seq(
      "units" -> b.units,
      "root" -> b.root,
      "settings" -> b.settings.sortBy(renderNode),
      "data" -> b.data,
      "index" -> b.index,
      "streams" -> b.streams(state),
      "delegates" -> b.delegates,
      "scopeLocal" -> b.scopeLocal
    )
    case b: Load.LoadedBuildUnit => Seq(
      "unit" -> b.unit,
      "defined" -> b.defined,
      "rootProjects" -> b.rootProjects,
      "buildSettings" -> b.buildSettings
    ) 
    case s: Load.StructureIndex => Seq(
      "keyMap" -> s.keyMap,
      "taskToKey" -> s.taskToKey,
      "triggers" -> s.triggers,
      "keyIndex" -> s.keyIndex,
      "aggregateKeyIndex" -> s.aggregateKeyIndex
    )
    case s: Project.Setting[_] => Seq(
      "key" -> s.key,
      "init" -> s.init,
      "pos" -> s.pos
    ) 
    case p: ProjectDefinition[_] => Seq(
      "id" -> p.id, 
      "aggregate" -> p.aggregate,
      "base" -> p.base ,
      "configurations" -> p.configurations,
      "delegates" -> p.delegates,
      "dependencies" -> p.dependencies
    )
    case c: Configuration => Seq(
      "name" -> c.name,
      "description" -> c.description,
      "isPublic" -> c.isPublic,
      "extendsConfigs" -> c.extendsConfigs,
      "transitive" -> c.transitive
    )
    case t: Task[_] => Seq(
      "info" -> t.info,
      "work" -> t.work
    ) 
    case i: Info[_] => Seq(
      "name" -> i.name,
      "attributes" -> i.attributes
    ) 
    case k: Init[_]#ScopedKey[_] => Seq(
      "scope" -> k.scope,
      "key" -> k.key
    )  
    case i: Init[_]#Initialize[_] => Seq(
      "dependencies" -> i.dependencies
    ) 
    case s: Scope => Seq(
      "project" -> s.project,
      "config" -> s.config,
      "task" -> s.task,
      "extra" -> s.extra
    ) 
    case arr: Array[_] => arr.toList sortBy renderNode   
    case seq: Seq[_] => seq sortBy renderNode   
    case map: Map[_,_] => map.toSeq sortBy renderNode   
    case set: Set[_] => set.toSeq sortBy renderNode   
    case _ => Seq()
  }
  
  def renderNode(a: Any): String = a match {
    case (key, value) => renderNode(key) + ": " + renderNode(value)
    case AttributeEntry(key, value) => renderNode(key.label -> renderNode(value))
    case _: AppConfiguration => "AppConfiguration"
    case _: Load.BuildStructure => "BuildStructure"
    case _: Load.LoadedBuildUnit => "LoadedBuildUnit"
    case _: Load.StructureIndex => "StructureIndex"
    case _: SessionSettings => "SessionSettings"
    case _: Settings[_] => "Settings"
    case _: State => "State"
    case _: State.History => "History"
    case _: AttributeMap => "AttributeMap"
    case _: Command => "Command"         
    case s: Scope => (Seq(s.project, s.config, s.task, s.extra) map renderNode).mkString("Scope(", ", ", ")")
    case p: ProjectDefinition[_] => p.getClass.getSimpleName + "(" + p.id + ")"
    case c: Configuration => "Configuration(" + c.name + ")"
    case t: Task[_] => t.info.name getOrElse ""
    case i: Info[_] => "Info(" + i.name.getOrElse("") + ")"
    case _: Init[_]#ScopedKey[_] => "ScopedKey"
    case _: Init[_]#Initialize[_] => "Initialize"
    case s: Project.Setting[_] => s.key.key.label
    case _: Help => "Help"
    case seq: Seq[_] => "Seq(" + seq.length + ")"
    case set: Set[_] => "Set(" + set.size + ")"
    case map: Map[_,_] => "Map(" + map.size + ")"
    case arr: Array[_] => "Array(" + arr.length + ")"
    case x if genericLabel(x).length > 100 => genericLabel(x).substring(0, 100) + "..."
    case x => genericLabel(x)
  }
  
  private def genericLabel(x: Any) = x.toString
  
  expandRow(0)
}
