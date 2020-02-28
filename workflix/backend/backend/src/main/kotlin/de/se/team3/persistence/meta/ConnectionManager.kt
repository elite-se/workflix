package de.se.team3.persistence.meta

import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.support.postgresql.PostgreSqlDialect

object ConnectionManager {

    fun connect() {
        val server = System.getenv("workflix.db.server")
        val database = System.getenv("workflix.db.database")
        val user = System.getenv("workflix.db.user")
        val password = System.getenv("workflix.db.password")
        val url = "jdbc:postgresql://$server/$database"
        Database.connect(url, driver = "org.postgresql.Driver", dialect = PostgreSqlDialect(), user = user, password = password)
    }
}
