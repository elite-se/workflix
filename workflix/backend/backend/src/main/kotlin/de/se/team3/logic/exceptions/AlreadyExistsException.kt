package de.se.team3.logic.exceptions

/**
 * Indicates that a resource to be created already exists.
 */
class AlreadyExistsException(override val message: String) : Exception(message)
