package de.se.team3.logic.authentication

import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

object VerificationMailManager {


    fun sendVerificationMail(email: String) {
        val to = email
        val from = "verification@workflix.com"
        val host = "postout.lrz.de"

        //Get the session object
        val properties = System.getProperties()
        properties.setProperty("mail.smtp.host", host)
        val session = Session.getDefaultInstance(properties)

        //compose the message
        try{
            val message = MimeMessage(session)
            message.setFrom(InternetAddress(from))
            message.addRecipient(Message.RecipientType.TO,InternetAddress(to))
            message.subject = "Ping"
            message.setText("Hello, this is example of sending email  ")

            // Send message
            Transport.send(message)
            println("message sent successfully....")

        } catch (mex: MessagingException) {mex.printStackTrace()}
    }
}

fun main() {
    VerificationMailManager.sendVerificationMail("erik.pallas@student.uni-augsburg.de")
}