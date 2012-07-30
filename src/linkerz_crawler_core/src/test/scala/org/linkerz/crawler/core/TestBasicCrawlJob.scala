/*
 * Copyright (C) 2012 - 2013 LinkerZ
 */

package org.linkerz.crawler.core

import controller.CrawlerController
import handler.CrawlerHandler
import job.CrawlJob
import model.WebUrl
import org.scalatest.FunSuite
import grizzled.slf4j.Logging

/**
 * The Class TestBasicCrawlJob.
 *
 * @author Nguyen Duc Dung
 * @since 7/29/12, 2:47 AM
 *
 */

class TestBasicCrawlJob extends FunSuite with Logging {

  test("testBasicJob") {
    val controller = new CrawlerController
    val handler = new CrawlerHandler(5)
    //Limit retry
    handler.maxRetry = 10
    //Ide time
    handler.ideTime = 5000

    controller.handlers += handler

    controller.start()
    val job = new CrawlJob(new WebUrl("http://vnexpress.net/"))
    controller.add(job)
    controller.stop()
  }

}