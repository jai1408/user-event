package com.syf.develop.exception;

public class UserEventException extends RuntimeException {
	public UserEventException(String exMessage, Exception exception) {
		super(exMessage, exception);
	}

	public UserEventException(String exMessage) {
		super(exMessage);
	}
}
