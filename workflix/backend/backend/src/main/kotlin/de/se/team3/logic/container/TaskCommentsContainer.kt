package de.se.team3.logic.container

import de.se.team3.logic.domain.Task
import de.se.team3.logic.domain.TaskComment
import de.se.team3.logic.exceptions.NotFoundException
import de.se.team3.persistence.daos.TaskCommentsDAO
import de.se.team3.webservice.containerInterfaces.TaskCommentsContainerInterface

object TaskCommentsContainer : TaskCommentsContainerInterface {

    // Maps the id of a task comment to the id of the task it belongs to.
    private val taskCommentsTaskIdCache = HashMap<Int, Int>()

    /**
     * Returns the task the specified task comment belongs to.
     *
     * @throws NotFoundException Is thrown if the specified task comment does not exist.
     */
    private fun getTaskByTaskCommentId(taskCommentId: Int): Task {
        val taskId = if (taskCommentsTaskIdCache.containsKey(taskCommentId)) {
            taskCommentsTaskIdCache.get(taskCommentId)!!
        } else {
            TaskCommentsDAO.getTaskIdByTaskCommentId(taskCommentId)
                ?: throw NotFoundException("task comment does not exist")
        }
        return TasksContainer.getTask(taskId)
    }

    /**
     * Creates the given task comment.
     *
     * @throws NotFoundException Is thrown if the task specified in taskComment does not exist.
     * @throws NotFoundException Is thrown if the user specified in taskComment does not exist.
     */
    override fun createTaskComment(taskComment: TaskComment): Int {
        val task = TasksContainer.getTask(taskComment.taskId!!) // throws NotFoundException

        // TODO check user existence

        val newId = TaskCommentsDAO.createTaskComment(taskComment)
        taskCommentsTaskIdCache.put(newId, taskComment.taskId!!)

        // update the task the comment belongs to
        task.addTaskComment(taskComment.copy(id = newId))

        return newId
    }

    /**
     * Updates the given task comment.
     *
     * @throws NotFoundException Is thrown if the given task comment does not exist.
     */
    override fun updateTaskComment(taskComment: TaskComment) {
        val task = getTaskByTaskCommentId(taskComment.id!!)

        val exists = TaskCommentsDAO.updateTaskComment(taskComment)
        if (!exists)
            throw NotFoundException("task comment not found")

        val currentTaskComment = task.getTaskComment(taskComment.id!!)
        currentTaskComment.setContent(taskComment.getContent())
    }

    /**
     * Deletes the specified task comment.
     *
     * @throws NotFoundException Is thrown if the specified task comment does not exist.
     */
    override fun deleteTaskComment(taskCommentId: Int) {
        val task = getTaskByTaskCommentId(taskCommentId)

        val existed = TaskCommentsDAO.deleteTaskComment(taskCommentId)
        if (!existed)
            throw NotFoundException("task comment not found")

        task.deleteTaskComment(taskCommentId)
        taskCommentsTaskIdCache.remove(taskCommentId)
    }
}
