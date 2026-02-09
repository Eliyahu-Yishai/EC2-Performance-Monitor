package com.aws_monitor.common.errors.safety;

import org.springframework.stereotype.Component;

@Component
public class SafetyPolicy {
    private boolean preventTermination = true;

    public boolean isTerminationAllowed() {
        return !preventTermination;
    }
}
