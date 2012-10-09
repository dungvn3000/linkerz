/*
 * Copyright (C) 2012 - 2013 LinkerZ (Searching and Sharing)
 */

package org.linkerz.language.detect.vietnamese

/**
 * The Class StopWordFilter.
 *
 * @author Nguyen Duc Dung
 * @since 8/8/12, 4:46 AM
 *
 */

object StopWordFilter {

  val world = Array("tôi", "bạn", "mày", "tao", "thì", "là", "cho", "của",
    "anh", "đã", "ông", "bà", "có", "thể", "các", "sau", "khi", "trước",
    "tại", "đây", "cô", "gái", "con", "trai", "em", "bé", "liên", "hệ",
    "lạc", "dành", "cho", "không", "thế", "nào")

  def isStopWord(string: String) = {
    world.contains(string)
  }
}
