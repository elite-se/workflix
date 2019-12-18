package de.se.team3.persistence.meta

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.support.postgresql.PostgreSqlDialect

object ConnectionManager {

    fun connect() {
        val server = "***REMOVED***"
        val database = "***REMOVED***"
        val user = "***REMOVED***"
        val password = "***REMOVED***"
        Database.connect(
            "jdbc:postgresql://" + server + "/" + database + "?user=" + user + "&password=" + password,
            driver = "org.postgresql.Driver",
            dialect = PostgreSqlDialect()
        )
    }
}
