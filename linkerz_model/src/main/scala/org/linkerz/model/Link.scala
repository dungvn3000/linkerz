/*
 * Copyright (C) 2012 - 2013 LinkerZ (Searching and Sharing)
 */

package org.linkerz.model

import java.util.Date

/**
 * The Class Linker.
 *
 * @author Nguyen Duc Dung
 * @since 8/3/12, 6:49 PM
 *
 */

class Link {

  var id: String = _

  var url: String = _
  var content: Array[Byte] = _
  var responseCode: Int = _

  //Metadata
  var text: String = _
  var contentEncoding: String = _
  var title: String = _

  //  description: Option[String] = None,
  var featureImageUrl: String = _
  //Feature Image
  var featureImage: Array[Byte] = _

  var parsed: Boolean = false

  var indexDate: Date = new Date


  override def equals(obj: Any) = {
    obj.isInstanceOf[Link] && obj.asInstanceOf[Link].url == url
  }

  override def hashCode() = url.hashCode
}