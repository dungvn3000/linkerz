/*
 * Copyright (C) 2012 - 2013 LinkerZ (Searching and Sharing)
 */

package org.linkerz.job.queue.handler

import org.linkerz.job.queue.job.EmptyJob
import org.linkerz.job.queue.session.SimpleSession
import org.linkerz.job.queue.core.Job
import org.linkerz.job.queue.actor.LazyWorker
import akka.actor.{ActorContext, Props}
import akka.routing.RoundRobinRouter

/**
 * The Class AsyncTestHandler.
 *
 * @author Nguyen Duc Dung
 * @since 8/23/12, 6:29 AM
 *
 */
class AsyncTestHandler extends AsyncHandler[EmptyJob, SimpleSession] {

  override protected def createWorker(context: ActorContext) = context.actorOf(Props[LazyWorker].withRouter(RoundRobinRouter(500)))

  protected def onSuccess(job: EmptyJob) {
    currentJob.count += 1
    //Making 1000 sub job for testing
    for (i <- 0 to 999) {
      this ! new EmptyJob(job)
    }
  }

  def sessionClass = classOf[SimpleSession]

  def accept(job: Job) = job.isInstanceOf[EmptyJob]
}
