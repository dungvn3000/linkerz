/*
 * Copyright (C) 2012 - 2013 LinkerZ (Searching and Sharing)
 */

package vn.myfeed.model

import org.joda.time.DateTime
import com.mongodb.casbah.Imports._

/**
 * The Class Link.
 *
 * @author Nguyen Duc Dung
 * @since 8/3/12, 6:49 PM
 *
 */

case class News(
                 //this is a url after normalization.
                 _id: String,
                 feedId: ObjectId,
                 url: String,
                 //Metadata
                 title: String,
                 description: String,
                 //This form rss.
                 descriptionHtml: String,
                 text: Option[String] = None,
                 html: Option[String] = None,
                 score: Double = 0d,

                 createdDate: DateTime = DateTime.now()
                 ) extends BaseModel(_id) {

  override def equals(obj: Any) = {
    obj.isInstanceOf[News] && obj.asInstanceOf[News].url == url
  }

  override def hashCode() = url.hashCode
}