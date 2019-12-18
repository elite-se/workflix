package de.se.team3.persistence.meta

import me.liuwj.ktorm.schema.*

object UsersTable : Table<Nothing>("users") {
    val ID by varchar("id").primaryKey()
    val name by varchar("name")
    val displayname by varchar("displayname")
    val email by varchar("email")
}

object ProcessTemplatesTable : Table<Nothing>("process_templates") {
    val ID by int("id").primaryKey()
    val ownerID by varchar("owner_id")
    val title by varchar("title")
    val durationLimit by int("duration_limit")
    val deleted by boolean("deleted")
}

object TaskTemplatesTable : Table<Nothing>("task_templates") {
    val ID by int("id").primaryKey()
    val templateID by int("template_id")
    val name by varchar("name")
    val estimatedDuration by int("estimated_duration")
    val durationLimit by int("duration_limit")
}

object TaskTemplateRelationshipsTable : Table<Nothing>("task_template_relationships") {
    val ID by int("id").primaryKey()
    val predecessor by int("predecessor")
    val successor by int("successor")
}

object Processes: Table<Nothing>("processes") {
    val ID by int("id").primaryKey()
    val templateID by int("template_id")
    val starterID by int("starter_id")
    val groupID by int("group_id")
    val title by varchar("title")
    val status by varchar("status")
    val startedAt by timestamp("started_at")
}

object Tasks: Table<Nothing>("tasks") {
    val ID by int("id").primaryKey()
    val processID by int("process_id")
    val templateID by int("template_id")
    val simpleClosing by boolean("simple_closing")
    val startedAt by timestamp("started_at")
}

object PersonsResponsible: Table<Nothing>("persons_responsible") {
    val ID by int("id").primaryKey()
    val taskID by int("task_id")
    val responsibleUserID by int("responsible_user_id")
    val done by boolean("done")
    val doneAt by timestamp("done_at")
}
