package de.se.team3.persistence.daos

import de.se.team3.logic.DAOInterfaces.TaskAssigmentsDAOInterface
import de.se.team3.logic.domain.TaskAssignment
import de.se.team3.persistence.meta.TaskAssignmentsTable
import java.time.Instant
import me.liuwj.ktorm.dsl.and
import me.liuwj.ktorm.dsl.delete
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.insertAndGenerateKey
import me.liuwj.ktorm.dsl.update

/**
 * DAO for task assignments.
 */
object TaskAssignmentsDAO : TaskAssigmentsDAOInterface {

    /**
     * Creates the given task assignment.
     *
     * @return The generated id for the task assignment.
     */
    override fun createTaskAssignment(taskAssignment: TaskAssignment): Int {
        val generatedProcessId = TaskAssignmentsTable.insertAndGenerateKey { row ->
            row.taskId to taskAssignment.taskId
            row.assigneeId to taskAssignment.assigneeId
            row.createdAt to taskAssignment.createdAt
            row.doneAt to taskAssignment.getDoneAt()
        }
        return generatedProcessId as Int
    }

    /**
     * Closes the specified task assignment.
     *
     * @return True if the specified assignment existed.
     */
    override fun closeTaskAssignment(taskId: Int, assigneeId: String, closingTime: Instant): Boolean {
        val affectedRows = TaskAssignmentsTable.update { row ->
            row.doneAt to closingTime
            where {
                (row.taskId eq taskId) and
                (row.assigneeId eq assigneeId)
            }
        }
        return affectedRows != 0
    }

    /**
     * Deletes the specified task assignment.
     *
     * @return True if the specified task assignment existed.
     */
    override fun deleteTaskAssignment(taskId: Int, assigneeId: String): Boolean {
        val affectedRows = TaskAssignmentsTable.delete { row ->
            (row.taskId eq taskId) and
                    (row.assigneeId eq assigneeId)
        }
        return affectedRows != 0
    }
}
