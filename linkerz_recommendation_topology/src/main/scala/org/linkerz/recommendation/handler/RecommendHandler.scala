package org.linkerz.recommendation.handler

import grizzled.slf4j.Logging

/**
 * The Class RecommendHandler.
 *
 * @author Nguyen Duc Dung
 * @since 11/11/12 4:37 PM
 *
 */
class RecommendHandler extends Logging {

//  protected def doHandle(job: RecommendJob) {
//    info("Doing recommendation")
//    val users = UserDao.find(MongoDBObject.empty)
//    val links = LinkDao.find(MongoDBObject("parsed" -> true)).toList.sortWith((link1, link2) => {
//      link1.indexDate.compareTo(link2.indexDate) > 0
//    })
//    users.foreach(user => {
//      val userBox = NewBoxDao.findByUserId(user._id)
//      if (!userBox.isEmpty) {
//        val clickedLinks = NewBoxDao.getUserClicked(user._id)
//        if (!clickedLinks.isEmpty) {
//          val newLinks = links.filter(!userBox.contains(_)).toList
//          Recommendation.buildScoreTable(clickedLinks, newLinks, minScore = 0.4).foreach(r => r match {
//            case (link1Id, link2Id, score) => {
//              NewBoxDao.save(NewBox(
//                userId = user._id,
//                linkId = new ObjectId(link2Id)
//              ))
//              info("Send to " + user.userName + " " + link2Id + " " + score)
//            }
//          })
//        }
//      }
//    })
//  }
}