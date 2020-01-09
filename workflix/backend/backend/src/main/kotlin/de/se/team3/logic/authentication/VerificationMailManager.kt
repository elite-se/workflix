package de.se.team3.logic.authentication

import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

object VerificationMailManager {

    /**
     * Sends an E-Mail with a verification code to the given mail address.
     *
     * @param to mail address the verification is going to be sent to
     */
    fun sendVerificationMail(to: String) {
        val from = "verification@workflix.com"
        val host = "postout.lrz.de"
        val port = "587"

        //Get the session object
        val mailProperties = System.getProperties()
        mailProperties.setProperty("mail.smtp.host", host)
        mailProperties.setProperty("mail.smtp.port", port)
        mailProperties.put("mail.smtp.auth", true)
        mailProperties.put("mail.smtp.socketFactory.port", port)
        mailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
        mailProperties.put("mail.smtp.socketFactory.fallback", "false")
        mailProperties.put("mail.smtp.starttls.enable", "true")
        val session = Session.getDefaultInstance(mailProperties)

        //compose the message
        try {
            val message = MimeMessage(session)
            message.setFrom(InternetAddress(from))
            message.addRecipient(Message.RecipientType.TO,InternetAddress(to))
            message.subject = "Ping"
            message.setText("Hello, this is a mailing ping test.")

            // Send message
            Transport.send(message)
            println("message sent successfully...")

        } catch (mex: MessagingException) {mex.printStackTrace()}
    }
}

fun main() {
    VerificationMailManager.sendVerificationMail("erik.pallas@student.uni-augsburg.de")
}