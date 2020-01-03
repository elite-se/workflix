package de.se.team3.logic.domain

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import de.se.team3.logic.container.UserContainer
import de.se.team3.webservice.util.InstantSerializer
import de.se.team3.webservice.util.UserSerializer
import java.time.Instant
import kotlin.collections.ArrayList
import org.json.JSONArray
import org.json.JSONObject

class ProcessGroup(
    var id: Int?,
    @JsonSerialize(using = UserSerializer::class)
    var owner: User,
    var title: String,
    var description: String,
    @JsonSerialize(using = InstantSerializer::class)
    val createdAt: Instant,
    val members: MutableList<User>
) {

    constructor(id: Int, owner: User, title: String, createdAt: Instant) :
        this(id, owner, title, "", createdAt, ArrayList<User>())

    /**
     * Creates a new process group with no specified ID.
     */
    constructor(title: String, description: String, ownerID: String, createdAt: Instant) :
            this(null, UserContainer.getUser(ownerID), title, "", createdAt, ArrayList<User>())

    /**
     * Creates a new process group, and adds a given list of users to it.
     */
    constructor(id: Int, owner: User, title: String, createdAt: Instant, members: MutableList<User>) :
        this(id, owner, title, "", createdAt, members)

}
