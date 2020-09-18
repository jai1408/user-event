package com.syf.develop.exception;

public class UserEventException extends Exception {

    private final UserEventError userEventError;

    public UserEventException(String exMessage, UserEventError userEventError) {
        super(exMessage);
        this.userEventError = userEventError;

    }

    public UserEventError getUserEventError() {
        return userEventError;
    }
}
