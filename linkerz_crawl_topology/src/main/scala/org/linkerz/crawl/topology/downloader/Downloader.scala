/*
 * Copyright (C) 2012 - 2013 LinkerZ (Searching and Sharing)
 */

package org.linkerz.crawl.topology.downloader

import grizzled.slf4j.Logging
import org.linkerz.crawl.topology.job.CrawlJob

/**
 * The Class Downloader.
 *
 * @author Nguyen Duc Dung
 * @since 7/29/12, 1:07 AM
 *
 */

trait Downloader extends Logging {

  def download(crawlJob: CrawlJob)

}
