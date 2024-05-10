package com.example.panservice.ServiceImpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.panservice.Entity.Panrequest;
import com.example.panservice.Entity.pandto;
//import com.example.panservice.Response.Panresponse;

import com.example.panservice.Service.Panservice;
import com.example.panservice.Utils.PropertiesConfig;
import com.example.panservice.exception.InvalidEmptyPanException;
import com.example.panservice.exception.InvalidIndividualException;
import com.example.panservice.exception.InvalidPanNumberException;
import com.example.panservice.exception.Missingnumberexeception;

import com.example.panservice.exception.UnauthorizedException;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@Service
public class PanServiceImpl implements Panservice {

	private final RestTemplate restTemplate;

	private final String apiKey;

	private static final Logger logger = LoggerFactory.getLogger(PanServiceImpl.class);

	@Autowired
	PropertiesConfig propertiesConfig;

	@Autowired
	public PanServiceImpl(RestTemplate restTemplate, @Value("${api.key}") String apiKey) {
		this.restTemplate = restTemplate;
		this.apiKey = apiKey;
	}

	@Override
	public Object getPanDetails(pandto dto) {

		String APIURL = propertiesConfig.getPanApiURL();
		Panrequest panrequest = new Panrequest();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", apiKey); // Include API key directly without "Bearer" prefix

//		 String requestBody = "{\"number\": \"" + dto.getNumber() +
//		  "\", \"returnIndividualTaxComplianceInfo\": \"" +
//		  dto.getReturnIndividualTaxComplianceInfo() + "\"}";

		panrequest.setNumber(dto.getNumber());
		panrequest.setReturnIndividualTaxComplianceInfo(dto.getReturnIndividualTaxComplianceInfo());

		HttpEntity<String> entity = new HttpEntity(panrequest, headers);

//		Object response = restTemplate.postForObject(APIURL, request,Object.class);
//		return response;

		try {
			Object response = restTemplate.postForObject(APIURL, entity, Object.class);
			return response;
		} catch (HttpClientErrorException.BadRequest e) {
			String errorMessage = e.getResponseBodyAsString();

			logger.error("Error Response: {}", errorMessage);
			if (errorMessage.contains("PAN number is not valid")) {
				throw new InvalidPanNumberException("PAN number is not valid");
			} else {
				if (errorMessage.contains("number is not allowed to be empty.")) {
					throw new InvalidEmptyPanException("number is not allowed to be empty.");
				} else {
					if (errorMessage.contains("number must be a string")) {
						throw new Missingnumberexeception("number must be a string");
					} else {
						if (errorMessage.contains(
								"\\\"requestBody.returnIndividualTaxComplianceInfo\\\" is not allowed to be empty")) {
							throw new InvalidIndividualException(
									"\\\"requestBody.returnIndividualTaxComplianceInfo\\\" is not allowed to be empty");
						} else if (errorMessage.contains("401 Unauthorized: [no body]")) {
							throw new UnauthorizedException("401 Unauthorized: [no body]");
						} else {
							throw e;
						}

					}

				}
			}
		}
    }
	}
