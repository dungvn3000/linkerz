package org.linkerz.crawl.topology.downloader

import org.junit.Test
import org.linkerz.crawl.topology.job.CrawlJob
import org.linkerz.crawl.topology.parser.LinkerZParser

/**
 * The Class TestDefaultDownloader.
 *
 * @author Nguyen Duc Dung
 * @since 12/27/12 5:56 PM
 *
 */
class TestDefaultDownloader {

  @Test
  def testDownloader() {
    val downloader = new DefaultDownloader()
    val parser = new LinkerZParser
    val crawlJob = new CrawlJob("http://soha.vn/xa-hoi/xuan-da-ve-som-tren-vuon-dao-nhat-tan-20121227101219859.htm")
    downloader.download(crawlJob)
    parser.parse(crawlJob)

    assert(crawlJob.result.get.responseCode == 200)
    println(crawlJob.result.get.webUrls.size())
    println(crawlJob.result.get.title)
    println(crawlJob.result.get.description.get)

    crawlJob.result.get.potentialImagesUrl.foreach(println)
  }

}