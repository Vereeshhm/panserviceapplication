package com.example.panservice.exception;

public class InvalidIndividualException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidIndividualException(String message) {
        super(message);
    }
}
