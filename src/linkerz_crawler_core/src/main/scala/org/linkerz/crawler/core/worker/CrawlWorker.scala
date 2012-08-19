/*
 * Copyright (C) 2012 - 2013 LinkerZ
 */

package org.linkerz.crawler.core.worker

import org.linkerz.job.queue.core.Worker
import org.linkerz.crawler.core.job.CrawlJob
import org.linkerz.crawler.core.session.CrawlSession
import org.linkerz.crawler.core.fetcher.Fetcher
import org.linkerz.crawler.core.downloader.Downloader
import org.linkerz.crawler.core.parser.Parser

/**
 * The Class CrawlWorker.
 *
 * @author Nguyen Duc Dung
 * @since 7/29/12, 2:40 AM
 *
 */

class CrawlWorker(_id: Int, downloader: Downloader, imageDownloader: Downloader, parser: Parser) extends Worker[CrawlJob, CrawlSession] {

  val fetcher = new Fetcher(downloader, imageDownloader, parser)

  def analyze(job: CrawlJob, session: CrawlSession) {
    fetcher.fetch(job)
  }

  def id = _id
}
