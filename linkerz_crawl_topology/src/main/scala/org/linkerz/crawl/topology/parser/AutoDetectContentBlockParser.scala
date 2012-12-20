package org.linkerz.crawl.topology.parser

import cleaner._
import core.TextBlock
import extractor.{ImageExtractor, TitleExtractor}
import net.htmlparser.jericho.{Element, HTMLElementName, Source}
import java.io.ByteArrayInputStream
import org.apache.commons.lang.StringUtils
import scala.collection.JavaConversions._
import collection.mutable.ListBuffer
import collection.mutable
import org.linkerz.crawl.topology.job.CrawlJob

/**
 * The Class AutoDetectContentBlockParser.
 *
 * @author Nguyen Duc Dung
 * @since 12/19/12 12:22 PM
 *
 */
class AutoDetectContentBlockParser extends Parser {

  val minTextLength = 10
  val minStopWordCount = 2
  val minTitleCount = 1

  def parse(crawlJob: CrawlJob) {

    //Step1: Parser the html page.
    val inputStream = new ByteArrayInputStream(crawlJob.result.get.content)
    var source = new Source(inputStream)
    source = Cleaner.clean(source)
    source.fullSequentialParse()

    val title = TitleExtractor.extract(source)

    if (title.isDefined) {
      val potentialBlocks = findPotentialBlock(source.getAllElements)

      if (!potentialBlocks.isEmpty) {
        val parentMap = new mutable.HashMap[TextBlock, Int].withDefaultValue(0)
        potentialBlocks.foreach(block => {
          parentMap(TextBlock(block.parent)) += 1
        })

        val sortedParent = parentMap.toList.sortWith(_._2 > _._2).map(_._1)

        val bestBlock = findTheBestBlock(sortedParent, title.get)

        info("title: " + title.get)

        val imgElement = ImageExtractor.extract(bestBlock.element)

        imgElement.map(img => {
          info("image: " + img.getAttributeValue("src"))
        })

        info(bestBlock.element.getStartTag)
        info(bestBlock.text)
      }
    } else {
      info("Can't extract title form your article, you page might is a home page is not an individual article")
    }
  }

  /**
   * Base on the title, make sure the content has some thing relate with the title.
   * @param sortedParent
   * @param title
   * @return
   */
  private def findTheBestBlock(sortedParent: List[TextBlock], title: String): TextBlock = {
    sortedParent.foreach(block => {
      if (block.count(title) >= minTitleCount) {
        return block
      }
    })
    sortedParent.head
  }


  private def findPotentialBlock(elements: java.util.List[Element]) = {
    val potentialBlocks = new ListBuffer[TextBlock]
    elements.foreach(el => {
      if (el.getName != HTMLElementName.BODY
        && el.getName != HTMLElementName.HTML
        && el.getName != HTMLElementName.TITLE
        && el.getName != HTMLElementName.HEAD
        && el.getName != HTMLElementName.META
        && el.getName != HTMLElementName.A) {
        val textBlock = TextBlock(el)
        if (StringUtils.isNotBlank(textBlock.textEvaluate) && textBlock.textEvaluate.length > minTextLength) {
          if (textBlock.stopWordCount >= minStopWordCount) {
            potentialBlocks += textBlock
          }
        }
      }
    })
    potentialBlocks.toList
  }
}
