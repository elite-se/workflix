package de.se.team3.logic.domain

import de.se.team3.logic.exceptions.InvalidInputException
import de.se.team3.logic.util.RelevantProcessQuerying

data class ProcessQueryPredicate(
    val statuses: List<ProcessStatus>,
    val processGroupIds: List<Int>,
    val involvingUserId: String?
) {

    init {
        if (involvingUserId != null && involvingUserId != "" && involvingUserId.length != 24)
            throw InvalidInputException("invalid user id")

        processGroupIds.forEach { if (it < 1) throw InvalidInputException("process group id must be positive") }
    }

    /**
     * Checks if the given entry is contained in the list if the list ist not empty.
     *
     * @return True if and only if the list is empty or the list contains the given entry.
     */
    private fun <T> List<T>.containsOrEmpty(entry: T): Boolean {
        return if (this.isEmpty())
            true
        else
            this.contains(entry)
    }

    /**
     * Checks whether the given process fulfills the predicate.
     */
    fun satisfiedBy(process: Process): Boolean {
        if (statuses.isEmpty() && processGroupIds.isEmpty() && involvingUserId == null)
            return true

        if (statuses.containsOrEmpty(process.getStatus())
            && processGroupIds.containsOrEmpty(process.processGroupId)
            && RelevantProcessQuerying.get(involvingUserId).containsOrEmpty(process.id))
            return true

        return false
    }
}
