package org.errorzhu.flow.base.exception;

public class FlowConfigException extends IllegalArgumentException{

    public FlowConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public FlowConfigException(String s) {
        super(s);
    }
}
