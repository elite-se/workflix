package de.se.team3.persistence

import de.se.team3.logic.domain.Process
import de.se.team3.persistence.daos.ProcessDAO
import de.se.team3.persistence.meta.ConnectionManager

fun main() {
    ConnectionManager.connect()

    val title = "My Process"
    val description = "My description that is longer than the title"
    val processTemplateId = 1
    val starterId = "58c120552c94decf6cf3b700"

    val simpleClosingMap = HashMap<Int, Boolean>()
    for (k in 1 .. 5)
        simpleClosingMap.put(k, true)

    val personsResponsible = HashSet<String>()
    personsResponsible.add("58c120552c94decf6cf3b700")
    val personsResponsibleIds = HashMap<Int, Set<String>>()
    for (k in 1 .. 5)
        personsResponsibleIds.put(k, personsResponsible)

    val process = Process(title, description, processTemplateId, starterId, simpleClosingMap, personsResponsibleIds)

    ProcessDAO.createProcess(process)

    println("Hallo Welt")

    /*val users = UserDAO.getUsers(0, 100).first
    for (user in users) {
        println("name: " + user.name)
        println("email: " + user.email)
    }*/
}
