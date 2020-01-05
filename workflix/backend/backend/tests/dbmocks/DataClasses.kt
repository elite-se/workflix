package dbmocks

import java.time.Instant

class DataClasses {

    data class TaskCommentsRow(
        var id: Int,
        var taskId: Int,
        var creatorId: String,
        var content: String,
        var createdAd: java.time.Instant,
        var deleted: Boolean
    )

    data class TaskTemplateRelationshipsRow(
        var id: Int,
        var predecessor: Int,
        var successor: Int
    )

    data class TaskTemplatesRow(
        var id: Int,
        var processTemplateId: Int,
        var name: String,
        var estimatedDuration: Int,
        var description: String,
        var necessaryClosings: Int
    )

    data class TasksRow(
        var id: Int,
        var processId: Int,
        var taskTemplateId: Int,
        var startedAt: java.time.Instant
    )

    data class UserRoleMembersRow(
        var id: Int,
        var userId: String,
        var userRoleId: Int
    )

    data class UserRolesRow(
        var id: Int,
        var name: String,
        var description: String,
        var createdAt: Instant,
        var deleted: Boolean
    )

    data class UsersRow(
        var id: String,
        var name: String,
        var displayname: String,
        var email: String,
        var delted: Boolean,
        var createdAt: Instant,
        var password: String
    )

}