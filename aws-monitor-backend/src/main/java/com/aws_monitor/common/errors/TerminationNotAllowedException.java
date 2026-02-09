package com.aws_monitor.common.errors;

/**
 * Exception thrown when an attempt is made to perform a destructive EC2 operation
 * (such as instance termination) while the safety guard is active.
 *
 * This exception indicates that the operation was blocked by the system's
 * safety policy, which enforces READ-ONLY mode by default.
 *
 * HTTP Status: 403 Forbidden
 * Reason: The operation is not permitted by the current safety configuration.
 *
 * @author AWS Monitor Team
 * @since 1.0
 */
public class TerminationNotAllowedException extends ApplicationException {

    public TerminationNotAllowedException(String message) {
        super(message);
    }
}
