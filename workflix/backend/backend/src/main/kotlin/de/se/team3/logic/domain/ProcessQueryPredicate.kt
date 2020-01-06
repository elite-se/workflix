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
     *
     * Let status_1, ..., status_n be the elements of statuses, let
     * processGroupId_1, ..., processGroupId_m be the elements of processGroupIds
     * and let relevantProcessId_1, ..., relevantProcessId_k be the ids of
     * relevant processes computed by RelevantProcessQuerying. Then the predicate to
     * be satisfied looks like the following:
     *
     * (n == 0 or process.stauts == status_1 or ... process.status == status_n) and
     * (m == 0 or process.processGroupId == processGroupId_1 or ... or process.processGroupId == processGroupId_m) and
     * (involving == null or process.id == relevantProcessId_1 or ... or process.id == relevantProcessId_k)
     *
     * @return True if and only if the predicate is satisfied.
     */
    fun satisfiedBy(process: Process): Boolean {
        if (statuses.isEmpty() && processGroupIds.isEmpty() && involvingUserId == null)
            return true

        if (statuses.containsOrEmpty(process.getStatus()) &&
            processGroupIds.containsOrEmpty(process.processGroupId) &&
            RelevantProcessQuerying.isRelevantFor(involvingUserId, process))
            return true

        return false
    }
}
