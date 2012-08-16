/*
 * Copyright (C) 2012 - 2013 LinkerZ
 */

package org.linkerz.crawler.bot

import factory.ParserPluginFactory
import org.scalatest.FunSuite
import org.linkerz.test.spring.SpringContext
import plugin.vnexpress.VnExpressPlugin
import org.linkerz.crawler.core.fetcher.Fetcher
import org.linkerz.crawler.core.factory.DownloadFactory
import org.linkerz.crawler.core.job.CrawlJob

/**
 * The Class TestLinkerZParser.
 *
 * @author Nguyen Duc Dung
 * @since 8/11/12, 4:34 PM
 *
 */

class TestLinkerZParser extends FunSuite with SpringContext {

  test("testParser") {
    //Install the plugin first
    val parserFactory = context.getBean("parserFactory", classOf[ParserPluginFactory])
    parserFactory.install(classOf[VnExpressPlugin].getName)
    val downloadFactory = context.getBean("downloadFactory", classOf[DownloadFactory])
    val fetcher = new Fetcher(downloadFactory.createDownloader(), parserFactory.createParser)
    val crawlJob = new CrawlJob("http://vnexpress.net/gl/phap-luat/2012/08/dai-gia-dat-cang-bi-dieu-tra-lua-dao-1-000-ty-dong/")
    fetcher.fetch(crawlJob)
    val webPage = crawlJob.result.get
    println("link = " + webPage.webUrl.url)
    println("title = " + webPage.title)
    println("description = " + webPage.description)
    println("img = " + webPage.featureImageUrl)
  }
}
