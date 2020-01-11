package de.se.team3.webservice.managerInterfaces

interface VerificationMailManagerInterface {

    fun initVerification(mail: String)

    fun verifyAndInvalidateVerificationKey(mail: String, key: String): Boolean
}