package de.se.team3.persistence

import de.se.team3.persistence.meta.ConnectionManager

fun main() {
    ConnectionManager.connect()

    /*val users = UserDAO.getUsers(0, 100).first
    for (user in users) {
        println("name: " + user.name)
        println("email: " + user.email)
    }*/
}