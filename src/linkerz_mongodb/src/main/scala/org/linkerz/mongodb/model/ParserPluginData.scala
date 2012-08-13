/*
 * Copyright (C) 2012 - 2013 LinkerZ
 */

package org.linkerz.mongodb.model

/**
 * The Class ParserPlugin.
 *
 * @author Nguyen Duc Dung
 * @since 8/11/12, 4:49 PM
 *
 */

class ParserPluginData extends LinkerZEntity {

  var name: String = _
  var group: String = _
  var pluginClass: String = _
  var version: String = _
  var enable: Boolean = _

  /**
   * Using simple regex.
   * Only two wildcard
   *
   * (*) and (?)
   */
  var urlRegex: String = _

  var excludeUrl: String = _

  var titleSelection: String = _
  var titleAttName: String = _
  var titleMaxLength: Int = _

  var descriptionSelection: String = _
  var descriptionAttName: String = _
  var descriptionMaxLength: Int = _

  var imgSelection: String = _

  var urlTest: String = _

}