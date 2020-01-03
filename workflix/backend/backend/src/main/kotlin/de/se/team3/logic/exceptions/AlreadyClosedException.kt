package de.se.team3.logic.exceptions

/**
 * Indicates that a task or a task assignment is already closed.
 */
class AlreadyClosedException(override val message: String) : Exception(message)
