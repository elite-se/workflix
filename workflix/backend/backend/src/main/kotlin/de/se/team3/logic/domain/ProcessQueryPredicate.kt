package de.se.team3.logic.domain

import de.se.team3.logic.exceptions.InvalidInputException

data class ProcessQueryPredicate(
    val statuses: ArrayList<ProcessStatus>,
    val processGroupIds: List<Int>,
    val involvingUserId: String?
) {

    init {
        if (involvingUserId != null && involvingUserId != "" && involvingUserId.length != 24)
            throw InvalidInputException("invalid user id")

        processGroupIds.forEach { if (it < 1) throw InvalidInputException("process group id must be positive") }
    }

}