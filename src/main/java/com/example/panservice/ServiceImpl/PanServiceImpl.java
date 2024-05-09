package com.example.panservice.ServiceImpl;

import java.net.URI;

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

import com.example.panservice.Entity.pandto;
import com.example.panservice.Response.Panresponse;

import com.example.panservice.Service.Panservice;
import com.example.panservice.Utils.PropertiesConfig;


import com.example.panservice.exception.InvalidPanNumberException;

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
	public Panresponse getPanDetails(pandto dto) {

//		panerrorstatus panerrorstatus = new panerrorstatus();

		String APIURL = propertiesConfig.getPanApiURL();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", apiKey); // Include API key directly without "Bearer" prefix
		String requestBody = "{\"number\": \"" + dto.getNumber() + "\", \"returnIndividualTaxComplianceInfo\": \""
				+ dto.getReturnIndividualTaxComplianceInfo() + "\"}";

		HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

		System.out.println("Requestbody  " + requestBody);
//		 Panresponse response = restTemplate.postForObject(APIURL, request, Panresponse.class);

		try {
			Panresponse response = restTemplate.postForObject(APIURL, request, Panresponse.class);
			return response;
		} catch (HttpClientErrorException.BadRequest e) {
			String errorMessage = e.getResponseBodyAsString();
			// System.out.println("Error Response: " + errorMessage);
			logger.error("Error Response: {}", errorMessage);
			if (errorMessage.contains("PAN number is not valid")) {
				throw new InvalidPanNumberException("PAN number is not valid");
			} else {

				throw e;
			}

		}

	}
}
