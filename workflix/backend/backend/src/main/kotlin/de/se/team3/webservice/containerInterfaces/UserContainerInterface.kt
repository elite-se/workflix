package de.se.team3.webservice.containerInterfaces

import de.se.team3.logic.domain.User

interface UserContainerInterface {

    /**
     * Returns a list of users specified by page as well as the number of the last page.
     */
    fun getUsers(page: Int): Pair<List<User>, Int>
}
