package sbtfrontend

import sbt._
import Keys._
import complete.DefaultParsers._

import org.slf4j.impl.SbtStaticLoggerBinder
import net.liftweb.common.{Box, Failure}
import net.liftweb.common.{Full => LiftFull}
import com.github.eirslett.maven.plugins.frontend.lib._

import FrontendPlugin.autoImport.FrontendKeys.{frontendFactory, nodeProxies}

object FrontendInputTask {
  def apply(
    key: InputKey[Unit],
    func: (FrontendPluginFactory, String) => Box[Unit]
  ): Def.Initialize[InputTask[Unit]] = {
    Def.inputTask {
      val args = spaceDelimited("<arg>").parsed
      val log = streams.value.log
      SbtStaticLoggerBinder.sbtLogger = log
      func((frontendFactory in key).value, args.mkString(" ")) match {
        case Failure(msg, LiftFull(e), _) => throw e
        case _ =>
      }
    }
  }
}

object FrontendProxyInputTask {
  def apply(
    key: InputKey[Unit],
    func: (FrontendPluginFactory, String, Seq[ProxyConfig.Proxy]) => Box[Unit]
  ): Def.Initialize[InputTask[Unit]] = {
    Def.inputTask {
      val args = spaceDelimited("<arg>").parsed
      val log = streams.value.log
      SbtStaticLoggerBinder.sbtLogger = log
      func(
        (frontendFactory in key).value,
        args.mkString(" "),
        (nodeProxies in key).value
      ) match {
        case Failure(msg, LiftFull(e), _) => throw e
        case _ =>
      }
    }
  }
}
