package com.example.panservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

	
	
	 @ExceptionHandler(InvalidPanNumberException.class)
	    public ResponseEntity<String> handleInvalidPanNumberException(InvalidPanNumberException ex) {
	        // Create a custom response body
	        String responseBody = "{\"error\": "
	        		+ "{\"name\": \"error\", "
	        		+ "\"message\": \"PAN number is not valid\", "
	        		+ "\"status\": \"Bad Request\", "
	        		+ "\"statusCode\": 400}}";
	        // Return the custom response with HTTP status code 400
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
	    }
}
