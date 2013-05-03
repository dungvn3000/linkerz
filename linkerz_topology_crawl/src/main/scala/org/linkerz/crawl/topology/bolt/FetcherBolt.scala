package org.linkerz.crawl.topology.bolt

import storm.scala.dsl.StormBolt
import org.linkerz.crawl.topology.event.Start
import grizzled.slf4j.Logging
import org.linkerz.crawl.topology.downloader.DefaultDownloader
import org.linkerz.crawl.topology.factory.{ParserFactory, DownloadFactory}
import org.linkerz.crawl.topology.parser.RssParser

/**
 * This bolt is simply download a url and emit it to a parser.
 *
 * @author Nguyen Duc Dung
 * @since 11/30/12 12:55 AM
 *
 */
class FetcherBolt extends StormBolt(outputFields = List("feedId", "event")) with Logging {

  @transient
  private var downloader: DefaultDownloader = _

  @transient
  private var parser: RssParser = _

  setup {
    downloader = DownloadFactory.createDownloader()
    parser = ParserFactory.createRssParser()
  }

  execute {
    implicit tuple => tuple matchSeq {
      case Start(job) => {
        val response = downloader.download(job)
        val entries = parser.parse(response)
        entries.foreach(entry => {
          tuple.emit(job.feed._id, entry)
        })
      }
    }
      tuple.ack()
  }
}
