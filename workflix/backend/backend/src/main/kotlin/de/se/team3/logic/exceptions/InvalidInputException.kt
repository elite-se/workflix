package de.se.team3.logic.exceptions

/**
 * Indicates any form of invalid input to a domain class.
 */
class InvalidInputException(override val message: String) : Exception(message)
