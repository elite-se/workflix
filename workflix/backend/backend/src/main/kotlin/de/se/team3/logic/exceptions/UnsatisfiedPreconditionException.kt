package de.se.team3.logic.exceptions

/**
 * Indicates that a necessary precondition for the action is not satisfied.
 */
class UnsatisfiedPreconditionException(override val message: String) : Exception(message)
