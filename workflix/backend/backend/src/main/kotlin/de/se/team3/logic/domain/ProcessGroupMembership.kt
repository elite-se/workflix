package de.se.team3.logic.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import de.se.team3.logic.container.ProcessGroupContainer
import de.se.team3.logic.exceptions.InvalidInputException

/**
 * Represents the membership a user in a process group.
 */
data class ProcessGroupMembership(
    val id: Int?,
    val processGroupId: Int,
    val memberId: String
) {

    @get:JsonIgnore
    val processGroup by lazy { ProcessGroupContainer.getProcessGroup(processGroupId) }

    /**
     * Create-Constructor
     *
     * @throws InvalidInputException Is thrown if at least one of the given ids has an invalid format.
     */
    constructor(processGroupId: Int, memberId: String)
            : this(null, processGroupId, memberId) {

        if (processGroupId < 1)
            throw InvalidInputException("process group id must be positive")
        if (memberId.length != 24)
            throw InvalidInputException("invalid member id")
    }

}