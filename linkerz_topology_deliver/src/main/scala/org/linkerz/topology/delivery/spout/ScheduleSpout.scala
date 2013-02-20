package org.linkerz.topology.delivery.spout

import storm.scala.dsl.StormSpout
import grizzled.slf4j.Logging
import org.linkerz.core.actor.Akka
import akka.actor.Props
import backtype.storm.utils.Utils
import org.linkerz.topology.delivery.actor.ScheduleActor
import scala.concurrent.duration._

/**
 * The Class ScheduleSpout.
 *
 * @author Nguyen Duc Dung
 * @since 1/3/13 12:54 AM
 *
 */
class ScheduleSpout extends StormSpout(outputFields = List("userId", "event")) with Logging {

  setup {
    val scheduleActor = Akka.system.actorOf(Props(new ScheduleActor(_collector)))
    //Schedule the actor run for every 15 minutes.
    import Akka.system.dispatcher
    Akka.system.scheduler.schedule(10 seconds, 5 minutes, scheduleActor, "run")
  }

  def nextTuple() {
    //Sleep 10ms for not wasting cpu
    Utils sleep 10
  }
}
