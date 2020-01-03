package de.se.team3.logic.exceptions

/**
 * Indicates that a requested resource or a resource a request depends on does not exist.
 */
class NotFoundException(override val message: String) : Exception(message)
