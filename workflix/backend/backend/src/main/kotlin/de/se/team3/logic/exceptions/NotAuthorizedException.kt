package de.se.team3.logic.exceptions

/**
 * Indicates that an unauthorized user has tried to .
 */
class NotAuthorizedException(override val message: String) : Exception(message)
