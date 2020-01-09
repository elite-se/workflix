package de.se.team3.persistence.meta

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.support.postgresql.PostgreSqlDialect

object ConnectionManager {

    fun connect() {
        val server = "localhost" // "***REMOVED***"
        val database = "postgres" // "***REMOVED***"
        val user = "postgres" // "***REMOVED***"
        val password = "Iv\\Z[4x,QY0(" // "***REMOVED***"
        Database.connect(
            "jdbc:postgresql://" + server + "/" + database + "?user=" + user + "&password=" + password,
            driver = "org.postgresql.Driver",
            dialect = PostgreSqlDialect()
        )
    }
}
