package de.se.team3.persistence

import de.se.team3.persistence.meta.ConnectionManager
import de.se.team3.persistence.meta.TasksTable
import java.time.Instant
import me.liuwj.ktorm.dsl.batchInsert

fun main() {
    ConnectionManager.connect()

    val affectedRowsIds = TasksTable.batchInsert {
        item {
            it.processId to 5
            it.taskTemplateId to 1
            it.startedAt to Instant.now()
        }
    }

    println(affectedRowsIds)
}
