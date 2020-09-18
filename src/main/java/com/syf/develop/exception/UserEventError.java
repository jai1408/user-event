package com.syf.develop.exception;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UserEventError {
    INVALID_REQUEST("The request is invalid"),
    INVALID_TOKEN("Token is invalid, please try with valid token"),
    KEY_NOT_FOUND("key couldn't be retrieved from keystore"),
    MAIL_NOT_SEND("Mail not sent"),
    USER_ALREADY_REGISTERED("user is registered with this email"),
    USER_NOT_FOUND("");
    private final String text;

    UserEventError(String text) {
        this.text = text;
    }

    @JsonValue
    public String getText() {
        return text;
    }

    @JsonValue
    @Override
    public String toString() {
        return text;
    }
}

