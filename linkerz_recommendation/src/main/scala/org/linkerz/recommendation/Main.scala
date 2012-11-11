package org.linkerz.recommendation

import org.linkerz.model.LinkDao
import com.mongodb.casbah.commons.MongoDBObject
import org.linkerz.core.time.StopWatch
import redis.clients.jedis.Jedis
import collection.JavaConversions._
import org.bson.types.ObjectId

/**
 * The Class Main.
 *
 * @author Nguyen Duc Dung
 * @since 11/8/12 1:49 AM
 *
 */
object Main extends App with StopWatch {

  val links = LinkDao.find(MongoDBObject.empty).filter(_.featureImage.size > 0).toList

  //Assumption the user click on 10 links
  val userClickLinks = links.take(10)

  //And we got 250 news from the robot.
  val newestLink = links.takeRight(250)

  stopWatch("Build Score Table") {
    val scores = Recommendation.buildScoreTable(userClickLinks, newestLink, 3)
    scores.foreach(s => s match {
      case (id1, id2, score) => {
        val link1 = LinkDao.findOneById(new ObjectId(id1)).get
        val link2 = LinkDao.findOneById(new ObjectId(id2)).get
        info(link1.title + " - " + link2.title + " - " + score)
      }
    })
  }


}