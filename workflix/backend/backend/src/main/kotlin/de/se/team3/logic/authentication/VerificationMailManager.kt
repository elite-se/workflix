package de.se.team3.logic.authentication

import com.sendgrid.Method
import com.sendgrid.Request
import com.sendgrid.SendGrid
import com.sendgrid.helpers.mail.Mail
import com.sendgrid.helpers.mail.objects.Content
import com.sendgrid.helpers.mail.objects.Email
import de.se.team3.logic.container.UserContainer
import de.se.team3.logic.exceptions.InvalidInputException
import de.se.team3.logic.***REMOVED***connector.UserQuerying
import de.se.team3.webservice.managerInterfaces.VerificationMailManagerInterface

object VerificationMailManager : VerificationMailManagerInterface {

    // maps each mail address to its generated key
    // if the server resets, the key expires
    // TODO more secure expiring method
    private val keyMap = HashMap<String, String>()

    // API KEY necessary for mailing via SendGrid
    private val sendGridApiKey = System.getenv("workflix.sendgrid.key")

    /**
     * Generates a verification key and sends it to the respective user.
     *
     * @param mail email address of the user trying to sign up
     */
    override fun initVerification(mail: String) {
        if (UserQuerying.searchFor***REMOVED***User(mail) == null)
            throw InvalidInputException("There is no user with this email address.")
        if (UserContainer.getAllUsers().map { it.email }.contains(mail))
            throw InvalidInputException("This user already exists.")
        val charPool: List<Char> = ('A'..'Z') + ('0'..'9')
        val key = (1..8)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
        keyMap[mail] = key
        sendVerificationMail(mail, key)
    }

    /**
     * Checks whether a given key is valid for a user and if it is, invalidates it.
     *
     * @param mail email address of the user to be verified
     * @param key verification key to be validated
     *
     * @return true iff the key was valid
     */
    override fun verifyAndInvalidateVerificationKey(mail: String, key: String): Boolean {
        return if (keyMap[mail] == key) {
            keyMap.remove(mail, key)
            true
        } else
            false
    }

    /**
     * Sends an E-Mail with a verification code to the given mail address.
     *
     * @param toAddress mail address the verification is going to be sent to
     * @param key generated verification key
     */
    private fun sendVerificationMail(toAddress: String, key: String) {
        val sendgrid = SendGrid(sendGridApiKey)

        val from = Email("verification@workflix.com")
        val to = Email(toAddress)
        val subject = "Please verify your account"
        val content = Content("text/html", """<h1 style="color: #5e9ca0;"><span style="color: #808080;">Thank you for using Workflix!<span style="color: #ffffff;"></span></span></h1>
<p>In order to start working, you have to verify your account by entering your verification key.</p>
<p>Your verification key is:</p>
<h3 style="padding-left: 60px;"><span style="color: #808080;"><strong>$key</strong></span></h3>
<p>This key will expire in several minutes.</p>
<p>If your key has expired, simply restart the registration process and a new key will be sent to you.</p>
<p>&nbsp;</p>
<p>Your Workflix Team</p>
<p><strong>&nbsp;</strong></p>""")

        val mail = Mail(from, subject, to, content)

        val request = Request()
        request.setMethod(Method.POST)
        request.setEndpoint("mail/send")
        request.setBody(mail.build())
        sendgrid.api(request)
    }
}
