package de.se.team3.persistence.meta

import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.boolean
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.text
import me.liuwj.ktorm.schema.timestamp
import me.liuwj.ktorm.schema.varchar

object UsersTable : Table<Nothing>("users") {
    val ID by varchar("id").primaryKey()
    val name by varchar("name")
    val displayname by varchar("displayname")
    val email by varchar("email")
    val createdAt by timestamp("created_at")
    val password by varchar("password")
    val deleted by boolean("deleted")
}

object UserRolesTable : Table<Nothing>("user_roles") {
    val ID by int("id").primaryKey()
    val name by varchar("name")
    val description by varchar("description")
    val createdAt by timestamp("created_at")
    val deleted by boolean("deleted")
}

object UserRoleMembersTable : Table<Nothing>("user_role_members") {
    val ID by int("id").primaryKey()
    val userID by varchar("user_id")
    val userRoleID by int("user_role_id")
}

object ProcessTemplatesTable : Table<Nothing>("process_templates") {
    val id by int("id").primaryKey()
    val ownerId by varchar("owner_id")
    val title by varchar("title")
    val description by text("description")
    val durationLimit by int("duration_limit")
    val createdAt by timestamp("created_at")
    val formerVersion by int("former_version")
    val deleted by boolean("deleted")
}

object ProcessTemplatesView : Table<Nothing>("process_templates_plus") {
    val id by int("id").primaryKey()
    val ownerId by varchar("owner_id")
    val title by varchar("title")
    val description by text("description")
    val durationLimit by int("duration_limit")
    val createdAt by timestamp("created_at")
    val formerVersion by int("former_version")
    val processCount by int("process_count")
    val runningProcesses by int("running_processes")
    val deleted by boolean("deleted")
}

object TaskTemplatesTable : Table<Nothing>("task_templates") {
    val id by int("id").primaryKey()
    val processTemplateId by int("process_template_id")
    val responsibleUserRoleId by int("responsible_user_role_id")
    val name by varchar("name")
    val description by text("description")
    val estimatedDuration by int("estimated_duration")
    val necessaryClosings by int("necessary_closings")
}

object TaskTemplateRelationshipsTable : Table<Nothing>("task_template_relationships") {
    val id by int("id").primaryKey()
    val predecessor by int("predecessor")
    val successor by int("successor")
}

object ProcessGroupsTable : Table<Nothing>("process_groups") {
    val id by int("id").primaryKey()
    val ownerId by varchar("owner_id")
    val title by varchar("title")
    val description by text("description")
    val createdAt by timestamp("created_at")
    val deleted by boolean("deleted")
}

object ProcessGroupsMembersTable : Table<Nothing>("process_groups_members") {
    val id by int("id").primaryKey()
    val processGroupId by int("process_group_id")
    val memberId by varchar("member_id")
}

object ProcessesTable : Table<Nothing>("processes") {
    val id by int("id").primaryKey()
    val processTemplateId by int("process_template_id")
    val starterId by varchar("starter_id")
    val groupId by int("group_id")
    val title by varchar("title")
    val description by text("description")
    val status by varchar("status")
    val deadline by timestamp("deadline")
    val startedAt by timestamp("started_at")
}

object TasksTable : Table<Nothing>("tasks") {
    val id by int("id").primaryKey()
    val processId by int("process_id")
    val taskTemplateId by int("task_template_id")
    val startedAt by timestamp("started_at")
}

object TaskAssignmentsTable : Table<Nothing>("task_assignments") {
    val id by int("id").primaryKey()
    val taskId by int("task_id")
    val assigneeId by varchar("assignee_id")
    val createdAt by timestamp("created_at")
    val doneAt by timestamp("done_at")
}

object TaskCommentsTable : Table<Nothing>("task_comments") {
    val id by int("id").primaryKey()
    val taskId by int("task_id")
    val creatorId by varchar("creator_id")
    val content by text("content")
    val createdAt by timestamp("created_at")
    val deleted by boolean("deleted")
}
