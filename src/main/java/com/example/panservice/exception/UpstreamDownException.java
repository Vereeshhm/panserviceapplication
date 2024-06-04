package com.example.panservice.exception;

public class UpstreamDownException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public UpstreamDownException(String message) {
        super(message);
    }

}
