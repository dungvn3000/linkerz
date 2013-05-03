package org.linkerz.crawl.topology.bolt

import storm.scala.dsl.StormBolt
import collection.JavaConversions._
import org.linkerz.crawl.topology.model.WebUrl
import org.linkerz.core.matcher.SimpleRegexMatcher
import grizzled.slf4j.Logging
import backtype.storm.tuple.Tuple
import org.linkerz.crawl.topology.event._
import org.linkerz.crawl.topology.session.RichSession._
import java.util.UUID
import org.linkerz.dao.{LinkTestDao, FeedDao, BlackUrlDao, NewsDao}
import org.linkerz.crawl.topology.event.Handle
import org.linkerz.crawl.topology.session.CrawlSession
import org.linkerz.crawl.topology.event.Start
import org.linkerz.crawl.topology.job.CrawlJob
import org.linkerz.crawl.topology.filter.BlackUrlPattern

/**
 * The mission of this bolt will receive job from the feed spot and emit it to a fetcher. On the other hand this bolt
 * will receive result from parser and decide whether continue crawling or not.
 *
 *
 * @author Nguyen Duc Dung
 * @since 11/29/12 11:41 PM
 *
 */
class HandlerBolt extends StormBolt(outputFields = List("sessionId", "event")) with Logging {

  private var sessions: List[CrawlSession] = Nil

  execute {
    implicit tuple => tuple matchSeq {
      case Seq(sessionId: UUID, Start(parentJob)) => {
        //New sessions.
        val session = CrawlSession(sessionId, parentJob)
        session.queueUrls += parentJob.webUrl
        sessions ::= session
        tuple emit(session.id, Handle(parentJob))
        tuple.ack()
      }
      case Seq(sessionId: UUID, Parse(subJob)) => sessions ~> sessionId map (session => handle(session, subJob)) getOrElse {
        //When session id is none that mean this job is expired already, we will stop it.
        info("Session is expired " + subJob.webUrl)
      }
      case Seq(sessionId: UUID, Ack) => sessions = sessions end sessionId
      case Seq(sessionId: UUID, Fail) => sessions = sessions end sessionId
    }
  }

  private def handle(session: CrawlSession, subJob: CrawlJob)(implicit tuple: Tuple) {
    subJob.result.map(webPage => {

      if (subJob.depth > session.currentDepth) {
        session.currentDepth = subJob.depth
      }

      val blackUrlPattern = new BlackUrlPattern(session.job.blackUrls)

      for (webUrl <- webPage.webUrls if (shouldCrawl(session, webUrl, blackUrlPattern))) {

        //Counting
        session.subJobCount += 1

        //Store queue url.
        session.queueUrls add webUrl

        tuple emit(session.id, Handle(new CrawlJob(webUrl, subJob)))
      }
    })

    //Copy logging information from sub jobs.
    session.job.errors ++= subJob.errors
    session.job.infos ++= subJob.infos
    session.job.warns ++= subJob.warns

    tuple.ack()
  }

  private def shouldCrawl(session: CrawlSession, webUrl: WebUrl, blackUrlPattern: BlackUrlPattern): Boolean = {

    val job = session.job

    //Step 1: Checking whether go for the job or not
    if (job.maxSubJob > 0 && session.subJobCount >= job.maxSubJob) {
      return false
    }

    if (session.currentDepth >= job.maxDepth && job.maxDepth > 0) {
      return false
    }


    if (job.filterPattern.matcher(webUrl.toString).matches()) return false

    //Only crawl the url is match with url regex
    if (job.urlRegex.isDefined && !SimpleRegexMatcher.matcher(webUrl.toString, job.urlRegex.get)) {
      return false
    }

    //Not crawl the exclude url
    if (job.excludeUrl != null) job.excludeUrl.foreach(regex => {
      if (SimpleRegexMatcher.matcher(webUrl.toString, regex)) {
        return false
      }
    })

    //Black list check
    if (blackUrlPattern.matches(webUrl.toString)) {
      return false
    }

    //Only crawl in same domain.
    if (!job.onlyCrawlInSameDomain
      || (job.onlyCrawlInSameDomain && webUrl.domainName == session.domainName)) {
      //Make sure the url is not in the queue
      if (!session.queueUrls.contains(webUrl)) {
        if (session.job.feed.confirmed) {
          if (NewsDao.findByUrl(webUrl.toString).isEmpty) {
            return true
          }
        } else if (LinkTestDao.findByUrl(webUrl.toString).isEmpty) {
          return true
        }
      }
    }

    false
  }

}
