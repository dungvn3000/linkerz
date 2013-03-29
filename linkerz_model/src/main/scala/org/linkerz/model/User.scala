package org.linkerz.model

import org.bson.types.ObjectId

/**
 * The Class User.
 *
 * @author Nguyen Duc Dung
 * @since 8/3/12, 6:05 PM
 *
 */

case class User(
                 _id: ObjectId = new ObjectId,
                 username: String,
                 password: String,
                 email: String,
                 userGroups: List[ObjectId] = Nil
                 ) extends LinkerZModel(_id)