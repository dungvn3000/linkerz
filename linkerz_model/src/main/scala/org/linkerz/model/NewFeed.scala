/*
 * Copyright (C) 2012 - 2013 LinkerZ (Searching and Sharing)
 */

package org.linkerz.model

import org.springframework.data.annotation.Id
import java.util

/**
 * The Class Feeds.
 *
 * @author Nguyen Duc Dung
 * @since 8/12/12 7:47 PM
 *
 */

class NewFeed {
  @Id
  var id: String = _
  var name: String = _
  var group: String = _
  var url: String = _
  var enable: Boolean = _

  var regex: util.List[String] = _
  var ignore: util.List[String] = _

  var titleSelection: String = _
  var titleAttName: String = _
  var titleMaxLength: Int = _
  var contentSelection: String = _
  var imgSelection: String = _

  var urlTest1: String = _
  var title1: String = _
  var validateText1: String = _
  var imageUrl1: String = _

  var urlTest2: String = _
  var title2: String = _
  var validateText2: String = _
  var imageUrl2: String = _

  var urlTest3: String = _
  var title3: String = _
  var validateText3: String = _
  var imageUrl3: String = _
}