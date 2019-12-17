package de.se.team3.logic.domain

import de.se.team3.logic.***REMOVED***connector.UserQuerying

class User(id: String, name: String, displayname: String, email: String) {
    val id = id
    val name = name
    val displayname = displayname
    val email = email

    constructor(name: String, displayname: String, email: String): this("", name, displayname, email) {
        if (name.length == 0 || displayname.length == 0 || email.length == 0)
            throw IllegalArgumentException("not all arguments may be empty")
    }

    companion object {
        //queries the ***REMOVED*** API for ***REMOVED*** Users registered under the given e-mail address
        //throws exceptions if either the given e-mail address is not of a valid format, or no ***REMOVED*** User
        //with this address can be found
        //TODO: check whether user already exists
        fun query***REMOVED***andCreateUser(email: String): User {
            //checks whether email is a (syntactically) valid e-mail address
            if (!email.matches(Regex("""^\w+@\w+..{2,3}(.{2,3})?$""")))
                throw java.lang.IllegalArgumentException("The e-mail address given is not of a valid format.")
            val user = UserQuerying.searchFor***REMOVED***User(email)
            if (user == null)
                throw java.lang.IllegalArgumentException("No user with this e-mail address exists.")
            return user
        }

        //TODO: create user without ***REMOVED*** querying, generating a new unique ID
    }

}

fun main() {
    println(User.query***REMOVED***andCreateUser("e@a.pd").displayname)
}