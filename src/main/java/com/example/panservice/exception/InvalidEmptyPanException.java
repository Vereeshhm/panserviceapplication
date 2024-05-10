package com.example.panservice.exception;

public class InvalidEmptyPanException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidEmptyPanException(String message) {
        super(message);
    }
}
