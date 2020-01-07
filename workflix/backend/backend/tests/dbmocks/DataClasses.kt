package dbmocks

import de.se.team3.logic.domain.ProcessStatus
import java.time.Instant

data class ProcessGroupsRow (
    var id: Int,
    var ownerId: String,
    var title: String,
    var description: String,
    var createdAt: Instant,
    var delted: Boolean
)

data class ProcessGroupsMembersRow (
    var id: Int,
    var processGroupId: Int,
    var memberId: String
)

data class ProcessTemplatesRow (
    var id: Int,
    var ownerId: String,
    var title: String,
    var description: String,
    var durationLimit: Int,
    var delted: Boolean,
    var createdAt: Instant,
    var formerId: Int
)

data class ProcessesRow (
    var id: Int,
    var processTemplateId: Int,
    var starterId: String,
    var groupId: Int,
    var title: String,
    var description: String,
    var status: ProcessStatus,
    var startedAt: Instant,
    var deadline: Instant
)

data class TaskAssignmentsRow (
    var id: Int,
    var taskId: Int,
    var assigneeId: String,
    var createdAt: Instant,
    var doneAt: Instant
)

data class TaskCommentsRow(
    var id: Int,
    var taskId: Int,
    var creatorId: String,
    var content: String,
    var createdAd: Instant,
    var deleted: Boolean
)

data class TaskTemplateRelationshipsRow (
    var id: Int,
    var predecessor: Int,
    var successor: Int
)

data class TaskTemplatesRow (
    var id: Int,
    var processTemplateId: Int,
    var name: String,
    var estimatedDuration: Int,
    var description: String,
    var necessaryClosings: Int
)

data class TasksRow (
    var id: Int,
    var processId: Int,
    var taskTemplateId: Int,
    var startedAt: Instant
)

data class UserRoleMembersRow (
    var id: Int,
    var userId: String,
    var userRoleId: Int
)

data class UserRolesRow (
    var id: Int,
    var name: String,
    var description: String,
    var createdAt: Instant,
    var deleted: Boolean
)

data class UsersRow (
    var id: String,
    var name: String,
    var displayname: String,
    var email: String,
    var deleted: Boolean,
    var createdAt: Instant,
    var password: String
)