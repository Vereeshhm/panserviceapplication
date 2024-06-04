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
		String responseBody = "{\"error\": " + "{\"name\": \"error\", " + "\"message\": \"PAN number is not valid\", "
				+ "\"status\": \"Bad Request\", " + "\"statusCode\": 400}}";
		// Return the custom response with HTTP status code 400
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
	}

	@ExceptionHandler(Missingnumberexeception.class)
	public ResponseEntity<String> handleIllegalArgumentException1(Missingnumberexeception ex) {
		String errorMessage = ex.getMessage();
		if (errorMessage.equals("number must be a string")) {
			// Create a custom response body
			String responseBody = "{\"error\": " + "{\"name\": \"error\", " + "\"message\": \"" + errorMessage + "\", "
					+ "\"status\": \"Bad Request\", " + "\"statusCode\": 400}}";
			// Return the custom response with HTTP status code 400
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
		}
		// If the message doesn't match, return a generic error response
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Internal Server Error\"}");
	}

	@ExceptionHandler(InvalidEmptyPanException.class)
	public ResponseEntity<String> handleIllegalArgumentException(InvalidEmptyPanException ex) {
		String errorMessage = ex.getMessage();
		if (errorMessage != null && (errorMessage.equals("number is required")
				|| errorMessage.equals("number is not allowed to be empty."))) {
			// Create a custom response body
			String responseBody = "{\"error\": " + "{\"name\": \"error\", " + "\"message\": \"" + errorMessage + "\", "
					+ "\"status\": \"Bad Request\", " + "\"statusCode\": 400}}";
			// Return the custom response with HTTP status code 400
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
		}
		// If the message doesn't match, return a generic error response
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Internal Server Error\"}");
	}

	@ExceptionHandler(InvalidIndividualException.class)
	public ResponseEntity<String> handleIllegalArgumentException3(InvalidIndividualException ex) {
		String errorMessage = ex.getMessage();
		if (errorMessage.equals("\\\"requestBody.returnIndividualTaxComplianceInfo\\\" is not allowed to be empty")) {
			// Create a custom response body
			String responseBody = "{\"error\": " + "{\"name\": \"error\", " + "\"message\": \"" + errorMessage + "\", "
					+ "\"status\": \"Bad Request\", " + "\"statusCode\": 400}}";
			// Return the custom response with HTTP status code 400
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
		}
		// If the message doesn't match, return a generic error response
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Internal Server Error\"}");
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<Object> handleNoApiKeyException(UnauthorizedException ex) {

		if (ex.getMessage().equals("[no body]")) {
			throw new UnauthorizedException("[no body]");
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
		}
	}

	@ExceptionHandler(UpstreamDownException.class)
	public ResponseEntity<Object> handleUpstreamDownException(UpstreamDownException ex) {
		String responseBody = "{\"error\":" + "{\"statusCode\":409," + "\"name\":\"error\"," + "\"message\":\""
				+ ex.getMessage() + "\"," + "\"status\":409," + "\"reason\":\"Error From Upstream\","
				+ "\"type\":\"Conflict\"" + "}}";
		return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
	}
}
