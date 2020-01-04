package de.se.team3.logic.domain.processQueryUtil

object RelevantProcessQuerying {

    /**
     * @return List of all processes which are processes of the given user's process group and the user's role is
     * assigned, or to which the user is assigned.
     */
    /*fun queryRelevantProcesses(user: User): List<Process> {
        val processesInUsersGroup = ProcessContainer.getAllProcesses()
            .filter { ProcessGroupsContainer
                .getProcessGroup(it.processGroupId)
                .getMembersIds()
                .contains(user.id) }
        val processesInUsersGroupItsRoleIsAssignedTo = processesInUsersGroup
            .filter { process ->
                process.tasks?.values?.filter { task ->
                user
                    .getUserRoleIds()
                    .contains(task.taskTemplate?.responsibleUserRoleId) }?.size ?: 0 != 0 }
        val processesUserIsAssignedTo = ProcessContainer.getAllProcesses().filter { it.getAssignees().contains(user) }
        return processesInUsersGroupItsRoleIsAssignedTo // + processesUserIsAssignedTo
    }

    /**
     * @return List of all processes which are processes of the given user's process group, or to which the user is
     * assigned.
     */
    fun queryProcessGroupProcesses(user: User): List<Process> {
        val processesInUsersGroup = ProcessContainer.getAllProcesses()
            .filter { ProcessGroupsContainer
                .getProcessGroup(it.processGroupId)
                .getMembersIds()
                .contains(user.id) }
        val processesUserIsAssignedTo = ProcessContainer.getAllProcesses().filter { it.getAssignees().map { it.id }.contains(user.id) }
        return processesInUsersGroup + processesUserIsAssignedTo
    }*/
}

/*fun main() {
    ConnectionManager.connect()
    val elias = User("58c120552c94decf6cf3b722", "Elias Keis", "EK", "ek@web.de", "r", Instant.now())
    val michael = User("58c120552c94decf6cf3b700", "Michael Markl", "MM", "mm@gmx.net", "b", Instant.now())
    val erik = User("22c120552c94decf6cf3b701", "Erik Pallas", "EP", "eigenlich ist der Shit hier hinten eh egal", Instant.now())
    val marvin = User("58c120552c94decf6cf3b701", "Marvin Brieger", "MB", "warum nochmal füll ich das so penibel aus", Instant.now())
    for (blubb in RelevantProcessQuerying.queryRelevantProcesses(michael)) {
        println(blubb.title)
    }
    /*println("all processes and their assignees:")
    for (process in ProcessContainer.getAllProcesses()) {
        println(process.title)
        for (assignee in process.getAssignees()) {
            val name = assignee.name
            println("   $name")
        }
    }*/
}*/
