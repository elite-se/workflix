package de.se.team3.persistence.meta

import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar

object Users : Table<Nothing>("users") {
    val ID by varchar("id").primaryKey()
    val name by varchar("name")
    val displayname by varchar("displayname")
    val email by varchar("email")
}

object ProcessTemplates : Table<Nothing>("process_templates") {
    val ID by int("id").primaryKey()
    val ownerID by varchar("owner_id")
    val title by varchar("title")
    val durationLimit by int("duration_limit")
}

object TaskTemplates : Table<Nothing>("task_templates") {
    val ID by int("id").primaryKey()
    val templateID by int("template_id")
    val name by varchar("name")
    val estimatedDuration by int("estimated_duration")
    val durationLimit by int("duration_limit")
}

object TaskTemplateRelationships : Table<Nothing>("task_template_relationships") {
    val ID by int("id").primaryKey()
    val predecessor by int("predecessor")
    val successor by int("successor")
}
