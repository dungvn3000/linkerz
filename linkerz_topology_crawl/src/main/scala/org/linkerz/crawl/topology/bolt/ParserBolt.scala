package org.linkerz.crawl.topology.bolt

import storm.scala.dsl.StormBolt
import org.linkerz.crawl.topology.event.{Fetch, Parse}
import org.linkerz.crawl.topology.parser.Parser
import org.linkerz.crawl.topology.factory.ParserFactory
import java.util.UUID

/**
 * This bolt will parse a web page.
 *
 * @author Nguyen Duc Dung
 * @since 11/30/12 1:00 AM
 *
 */
class ParserBolt extends StormBolt(outputFields = List("sessionId", "event")) {

  execute {
    implicit tuple => tuple matchSeq {
      case Seq(sessionId: UUID, Fetch(job)) => {
        try {
          val parser = ParserFactory.createParser(job.feeds)
          if (!job.isError) parser parse job
        } catch {
          case ex: Exception => {
            job.error(ex.getMessage, getClass.getName, ex)
            _collector reportError ex
          }
        }

        tuple emit(sessionId, Parse(job))
      }
    }
    tuple.ack()
  }
}
