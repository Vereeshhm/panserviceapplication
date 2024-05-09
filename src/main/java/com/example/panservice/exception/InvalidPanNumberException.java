package com.example.panservice.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class InvalidPanNumberException extends RuntimeException{
	
	public InvalidPanNumberException(String message) {
        super(message);
    }

}
