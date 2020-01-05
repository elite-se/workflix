package dbmocks

import de.se.team3.logic.domain.ProcessStatus
import java.time.Instant

class DataClasses {

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

    data class UserData (
        var id: String,
        var name: String,
        var displayname: String,
        var email: String,
        var delted: Boolean,
        var createdAt: Instant,
        var password: String
    )



}