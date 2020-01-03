package de.se.team3.persistence

import de.se.team3.persistence.daos.ProcessDAO
import de.se.team3.persistence.meta.ConnectionManager

fun main() {
    ConnectionManager.connect()

    /*val affectedRowsIds = TasksTable.batchInsert {
        item {
            it.processId to 5
            it.taskTemplateId to 1
            it.startedAt to Instant.now()
        }
    }*/

    ProcessDAO.closeProcess(39)
}
