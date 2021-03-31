package com.zh.ch.bigdata.base.util.exception;

/**
 * @author hadoop
 */
public class ProjectException extends Exception {

    public ProjectException() {
    }

    public ProjectException(String message) {
        super(message);
    }

    public ProjectException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProjectException(Throwable cause) {
        super(cause);
    }

    public ProjectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
