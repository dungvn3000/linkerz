package org.linkerz.parser.model

import org.jsoup.nodes.Element

/**
 * The Class LinkElement.
 *
 * @author Nguyen Duc Dung
 * @since 12/23/12 1:06 AM
 *
 */
case class LinkElement(override val jsoupElement: Element) extends ArticleElement(jsoupElement) {
  def text = jsoupElement.text()
}
