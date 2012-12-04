package org.linkerz.crawl.topology.bolt

import storm.scala.dsl.StormBolt
import org.linkerz.crawl.topology.event.{Handle, Persistent}
import java.util.UUID

/**
 * This bolt is using for persistent data to the database server.
 *
 * @author Nguyen Duc Dung
 * @since 11/30/12 2:12 AM
 *
 */
class PersistentBolt extends StormBolt(outputFields = List("sessionId", "event")) {
  execute {
    implicit tuple => tuple matchSeq {
      case Seq(sessionId: UUID, Persistent(job)) => {
        tuple emit(sessionId, Handle(job))
      }
    }
    tuple.ack()
  }
}
