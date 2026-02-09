package com.aws_monitor.safety;

import com.aws_monitor.common.errors.TerminationNotAllowedException;
import org.springframework.stereotype.Service;

/**
 * Safety guard that prevents destructive EC2 operations by default.
 * The system operates in READ-ONLY mode unless explicitly enabled.
 */
@Service
public class Ec2SafetyGuard {
    private boolean allowInstanceTermination;

    /**
     * Must be called before any destructive EC2 operation
     * (terminate, stop, or state modification).
     */
    public void checkTerminationAllowed() {
        if (!allowInstanceTermination) {
            throw new TerminationNotAllowedException(
                    "Destructive EC2 operations are disabled (READ-ONLY mode). " +
                            "Enable via aws.safety.allow-instance-termination=true."
            );
        }
    }

    /** Indicates whether destructive operations are allowed. */
    public boolean isTerminationAllowed() {
        return allowInstanceTermination;
    }

    /** Indicates whether the system is running in READ-ONLY mode. */
    public boolean isReadOnlyMode() {
        return !allowInstanceTermination;
    }
}
