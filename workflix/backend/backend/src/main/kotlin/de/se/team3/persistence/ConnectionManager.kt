package de.se.team3.persistence

import me.liuwj.ktorm.database.Database

object ConnectionManager {

    fun connect() {
        val server = "***REMOVED***"
        val database = "***REMOVED***"
        val user = "***REMOVED***"
        val password = "***REMOVED***"
        Database.connect("jdbc:postgresql://" + server + "/" + database + "?user=" + user + "&password=" + password,
            driver = "org.postgresql.Driver")
    }

}