package org.linkerz.crawl.topology.bolt

import storm.scala.dsl.StormBolt
import grizzled.slf4j.Logging
import org.linkerz.crawl.topology.model.WebPage
import org.bson.types.ObjectId
import org.linkerz.dao.LinkDao

/**
 * This bolt is using for persistent data to the database server.
 *
 * @author Nguyen Duc Dung
 * @since 11/30/12 2:12 AM
 *
 */
class PersistentBolt extends StormBolt(outputFields = Nil) with Logging {

  execute(implicit tuple => tuple matchSeq {
    case Seq(feedId: ObjectId, webPage: WebPage) => {
      webPage.feedId = feedId
      LinkDao.save(webPage.asLink)
    }
  })

}
