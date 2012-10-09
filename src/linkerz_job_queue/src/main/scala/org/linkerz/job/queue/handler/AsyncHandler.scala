/*
 * Copyright (C) 2012 - 2013 LinkerZ
 */

package org.linkerz.job.queue.handler

import org.linkerz.job.queue.core._
import org.linkerz.job.queue.core.Controller._
import grizzled.slf4j.Logging
import akka.actor._
import org.linkerz.job.queue.handler.AsyncHandler.Success
import org.linkerz.job.queue.handler.AsyncHandler.Fail
import org.linkerz.job.queue.handler.AsyncHandler.Next

object AsyncHandler {

  sealed trait Event

  case class Next[J <: Job, S <: Session[J]](job: J, session: S) extends Event

  case class Success[J <: Job](job: J) extends Event

  case class Fail[J <: Job](job: J, ex: Exception) extends Event

}

/**
 * The Class AsyncHandler.
 * The Handler with workers, the job will be do async in here.
 * Make sure the handler always has at least one worker.
 * The Worker will be created before handle a job.
 *
 * @author Nguyen Duc Dung
 * @since 7/9/12, 1:56 AM
 *
 */
abstract class AsyncHandler[J <: Job, S <: Session[J]] extends HandlerInSession[J, S] with Logging {

  /**
   * The current session.
   */
  protected var currentSession: S = _

  /**
   * The current job inside the session.
   */
  protected var currentJob: J = _

  protected implicit val workerManager = systemActor.actorOf(Props(new Actor {
    private val worker: ActorRef = createWorker(context)

    override protected def receive = {
      case job: J => {
        try {
          doJob(job)
        } catch {
          case ex: Exception => {
            error(ex.getMessage, ex)
            currentJob.error(ex.getMessage, ex)
          }
        }
      }
      case f: Fail[J] => {
        error(f.ex.getMessage, f.ex)
        currentJob.error(f.ex.getMessage, f.ex)
      }
      case s: Success[J] => onSuccess(s.job)
    }

    private def doJob(job: J) {
      //Step 1: Checking whether go for the job or not
      if (currentJob.maxSubJob >= 0 && currentSession.subJobCount >= currentJob.maxSubJob) {
        stop("Stop because the number of sub job reached maximum")
        return
      }

      if (currentSession.currentDepth > currentJob.maxDepth && currentJob.maxDepth > 0) {
        currentSession.currentDepth -= 1
        stop("Stop because the number of sub job reached maximum depth")
        return
      }

      //Checking working time.
      if (currentJob.timeOut > 0 && currentSession.jobTime > currentJob.timeOut) {
        //marking the job is error
        currentJob.error("Time Out")
        stop("Stop because the time is out")
        return
      }

      //Step 2: Find a free worker for the job.
      worker ! Next(job, currentSession)

      //Counting.
      currentSession.subJobCount += 1

      if (job.depth > currentSession.currentDepth) {
        currentSession.currentDepth = job.depth
      }

      //Delay time for each job.
      if (currentJob.politenessDelay > 0) Thread.sleep(currentJob.politenessDelay)
    }

    private def stop(reason: String) {
      info(reason)
      context.stop(self)
    }

  }), "workerManager")

  protected def doHandle(job: J, session: S) {
    //Step 1: Reset to start
    currentSession = session
    currentJob = job

    //Step 2: Send to the worker manager.
    workerManager ! job

    //Step 3: Waiting for all worker finished their job
    waitingForFinish()

    //Step 4: Finish.
    onFinish()
  }


  private def waitingForFinish() {
    while (!workerManager.isTerminated) {
      //Sleep 1s for next checking.
      Thread.sleep(1000)
    }
  }

  /**
   * Creating worker.
   * @param context
   * @return
   */
  protected def createWorker(context: ActorContext): ActorRef

  /**
   * This method will be called before the hander is going to finish everything.
   */
  protected def onFinish() {}

  /**
   * Create a sub job base the result of a job.
   * @param job
   */
  protected def onSuccess(job: J)
}
