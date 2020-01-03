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
    
    /**
     * Create-Constructor
     */
    constructor(title: String, description: String, ownerID: String) :
            this(null, UserContainer.getUser(ownerID), title, description, Instant.now(), ArrayList<User>())

}
