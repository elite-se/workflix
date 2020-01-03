package de.se.team3.logic.container

import de.se.team3.logic.domain.TaskComment
import de.se.team3.persistence.daos.TaskCommentsDAO
import de.se.team3.webservice.containerInterfaces.TaskCommentsContainerInterface

object TaskCommentsContainer : TaskCommentsContainerInterface {

    override fun createTaskComment(taskComment: TaskComment): Int {
        val newId = TaskCommentsDAO.createTaskComment(taskComment)

        // refresh process
        val task = TasksContainer.getTask(taskComment.taskId!!)
        val processId = task.process!!.id!!
        ProcessContainer.refreshCachedProcess(processId)

        return newId
    }

    override fun updateTaskComment(taskComment: TaskComment) {
        TaskCommentsDAO.updateTaskComment(taskComment)

        // refresh process
        val taskId = TaskCommentsDAO.getTaskIdByTaskCommentId(taskComment.id!!)
        val task = TasksContainer.getTask(taskId!!)
        val processId = task.process!!.id!!
        ProcessContainer.refreshCachedProcess(processId)
    }

    override fun deleteTaskComment(taskCommentId: Int) {
        TaskCommentsDAO.deleteTaskComment(taskCommentId)

        // refresh process
        val taskId = TaskCommentsDAO.getTaskIdByTaskCommentId(taskCommentId)
        val task = TasksContainer.getTask(taskId!!)
        val processId = task.process!!.id!!
        ProcessContainer.refreshCachedProcess(processId)
    }
}
