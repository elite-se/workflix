package dbmocks

import java.time.Instant

class DataClasses {

    data class UserData(
        var id: String,
        var name: String,
        var displayname: String,
        var email: String,
        var delted: Boolean,
        var createdAt: Instant,
        var password: String
    )



}