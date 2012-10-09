/*
 * Copyright (C) 2012 - 2013 LinkerZ (Searching and Sharing)
 */

package org.linkerz.web.components.search

import org.apache.tapestry5.annotations.{Component, Property}
import org.apache.tapestry5.corelib.components.{Form, TextField}
import org.linkerz.crawler.core.fetcher.Fetcher
import org.linkerz.web.services.db.DBStore
import org.apache.tapestry5.ioc.annotations.Inject
import org.linkerz.mongodb.model.{Link, User}
import org.linkerz.crawler.core.model.WebUrl
import org.linkerz.web.services.user.UserService
import org.linkerz.crawler.core.downloader.DefaultDownload
import org.linkerz.crawler.core.parser.DefaultParser

/**
 * The Class SearchBox.
 *
 * @author Nguyen Duc Dung
 * @since 8/3/12, 2:00 AM
 *
 */

class SearchBox {

  @Property
  private var keyWord: String = _

  @Component
  private var txtSearch: TextField = _

  @Component
  private var search: Form = _

  @Inject
  private var dbStore: DBStore = _

  @Inject
  private var userService: UserService = _

  def onSubmit() {
    println(keyWord)
  }
}
