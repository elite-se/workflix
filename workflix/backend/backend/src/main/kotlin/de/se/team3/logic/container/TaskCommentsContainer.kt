package de.se.team3.logic.container

import de.se.team3.logic.DAOInterfaces.TaskCommentsDAOInterface
import de.se.team3.logic.domain.Task
import de.se.team3.logic.domain.TaskComment
import de.se.team3.logic.domain.User
import de.se.team3.logic.exceptions.NotFoundException
import de.se.team3.persistence.daos.TaskCommentsDAO
import de.se.team3.persistence.meta.TaskCommentsTable.taskId
import de.se.team3.webservice.containerInterfaces.TaskCommentsContainerInterface

object TaskCommentsContainer : TaskCommentsContainerInterface {

    private val taskCommentsDAO: TaskCommentsDAOInterface = TaskCommentsDAO

    // Maps the id of a task comment to the id of the task it belongs to.
    private val taskCommentsTaskIdCache = HashMap<Int, Int>()

    // Maps the id of a task comment to the id of its creator
    private val taskCommentsCreatorIdCache = HashMap<Int, String>()

    /**
     * Returns the task the specified task comment belongs to.
     *
     * @throws NotFoundException Is thrown if the specified task comment does not exist.
     */
    override fun getTaskByTaskCommentId(taskCommentId: Int): Task {
        val taskId = if (taskCommentsTaskIdCache.containsKey(taskCommentId)) {
            taskCommentsTaskIdCache.get(taskCommentId)!!
        } else {
            taskCommentsDAO.getTaskIdByTaskCommentId(taskCommentId)
                ?: throw NotFoundException("task comment does not exist")
        }
        return TasksContainer.getTask(taskId)
    }

    /**
     * Returns the creator the specified task comment was created by.
     *
     * @throws NotFoundException Is thrown if the specified task comment does not exist.
     */
    override fun getCreatorByTaskCommentId(taskCommentId: Int): User {
        val creatorId = if (taskCommentsCreatorIdCache.containsKey(taskCommentId)) {
            taskCommentsCreatorIdCache.get(taskCommentId)!!
        } else {
            taskCommentsDAO.getCreatorIdByTaskCommentId(taskCommentId)
                ?: throw NotFoundException("task comment does not exist")
        }
        return UserContainer.getUser(creatorId)
    }

    /**
     * Creates the given task comment.
     */
    override fun createTaskComment(taskComment: TaskComment): Int {
        val task = TasksContainer.getTask(taskComment.taskId!!) // throws NotFoundException

        val newId = taskCommentsDAO.createTaskComment(taskComment)
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

        val exists = taskCommentsDAO.updateTaskComment(taskComment)
        if (!exists)
            throw NotFoundException("task comment not found")

        val currentTaskComment = task.getTaskComment(taskComment.id)
        currentTaskComment.setContent(taskComment.getContent())
    }

    /**
     * Deletes the specified task comment.
     *
     * @throws NotFoundException Is thrown if the specified task comment does not exist.
     */
    override fun deleteTaskComment(taskCommentId: Int) {
        val task = getTaskByTaskCommentId(taskCommentId)

        val existed = taskCommentsDAO.deleteTaskComment(taskCommentId)
        if (!existed)
            throw NotFoundException("task comment not found")

        task.deleteTaskComment(taskCommentId)
        taskCommentsTaskIdCache.remove(taskCommentId)
    }
}
