package de.se.team3.persistence

import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar

object Users : Table<Nothing>("users") {
    val id by varchar("id").primaryKey()
    val name by varchar("name")
    val displayname by varchar("displayname")
    val email by varchar("email")
}