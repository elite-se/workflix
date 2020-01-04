package de.se.team3.persistence.daos

import de.se.team3.logic.DAOInterfaces.UserDAOInterface
import de.se.team3.logic.domain.User
import de.se.team3.persistence.meta.UsersTable
import java.util.Arrays
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.collections.ArrayList
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.insert
import me.liuwj.ktorm.dsl.like
import me.liuwj.ktorm.dsl.select
import me.liuwj.ktorm.dsl.update
import me.liuwj.ktorm.dsl.where

object UserDAO : UserDAOInterface {

    // TODO store key safely
    private val key = SecretKeySpec(Arrays.copyOf("tHiSiSaVeRySeCuReKeY".toByteArray(), 16), "AES")

    private fun encryptPassword(password: String): String {
        //TODO fix this shit
        //val data = key.getEncoded()
        //val skeySpec = SecretKeySpec(data, 0, data.size, "AES")
        //val cipher = Cipher.getInstance("AES")
        //cipher.init(Cipher.ENCRYPT_MODE, skeySpec, IvParameterSpec(ByteArray(cipher.getBlockSize())))
        //return cipher.doFinal(password.toByteArray()).toString()
        return password.map { it.inc() }.toString()
    }

    private fun decryptPassword(encryptedPassword: String): String {
        //TODO fix this shit
        //val decrypted: ByteArray
        //val cipher = Cipher.getInstance("AES")
        //cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(ByteArray(cipher.blockSize)))
        //return cipher.doFinal(encryptedPassword.toByteArray()).toString()
        return encryptedPassword.map { it.dec() }.toString()
    }

    /**
     * {@inheritDoc}
     */
    override fun getAllUsers(): List<User> {
        val users = ArrayList<User>()
        val result = UsersTable.select()
        for (row in result)
            users.add(User(row[UsersTable.ID]!!, row[UsersTable.name]!!, row[UsersTable.displayname]!!, row[UsersTable.email]!!, decryptPassword(row[UsersTable.password]?:"N/A")))

        return users.toList()
    }

    override fun getUser(userId: String): User? {
        val result = UsersTable
            .select()
            .where { UsersTable.ID eq userId }

        val row = result.rowSet
        if (!row.next())
            return null
        return User(row[UsersTable.ID]!!, row[UsersTable.name]!!, row[UsersTable.displayname]!!, row[UsersTable.email]!!, decryptPassword(row[UsersTable.password]?:"N/A"))
    }

    override fun createUser(user: User) {
        UsersTable.insert {
            it.ID to user.id
            it.name to user.name
            it.displayname to user.displayname
            it.email to user.email
            it.password to encryptPassword(user.password)
            it.deleted to false
        }
    }

    override fun create***REMOVED***User(email: String, password: String): User {
        val ***REMOVED***User = User.query***REMOVED***andCreateUser(email, password)
        UsersTable.insert {
            it.ID to ***REMOVED***User.id
            it.name to ***REMOVED***User.name
            it.displayname to ***REMOVED***User.displayname
            it.email to ***REMOVED***User.email
            it.password to encryptPassword(***REMOVED***User.password)
            it.deleted to false
        }
        return ***REMOVED***User
    }

    /**
     * Updates the user data on basis of the given user's id.
     */
    override fun updateUser(user: User) {
        UsersTable.update {
            it.name to user.name
            it.displayname to user.displayname
            it.email to user.email
            it.password to encryptPassword(user.password)
            it.deleted to false

            where { it.ID like user.id }
        }
    }

    override fun deleteUser(user: User): Boolean {
        return UsersTable.update {
            it.deleted to true
            where { it.ID like user.id }
        } != 0
    }
}
