package com.example.panservice.ServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.panservice.Entity.ApiLog;
import com.example.panservice.Entity.pandto;
import com.example.panservice.Repository.ApiLogRepository;
import com.example.panservice.Service.Panservice;
import com.example.panservice.Utils.PropertiesConfig;
import com.example.panservice.exception.InvalidEmptyPanException;
import com.example.panservice.exception.InvalidIndividualException;
import com.example.panservice.exception.InvalidPanNumberException;
import com.example.panservice.exception.Missingnumberexeception;
import com.example.panservice.exception.UnauthorizedException;
import com.example.panservice.exception.UpstreamDownException;
import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class PanServiceImpl implements Panservice {

	private final RestTemplate restTemplate;

	private final String apiKey;

	@Autowired
	ApiLogRepository apiLogRepository;

	private static final Logger logger = LoggerFactory.getLogger(PanServiceImpl.class);

	@Autowired
	PropertiesConfig propertiesConfig;

	@Autowired
	public PanServiceImpl(RestTemplate restTemplate, @Value("${api.key}") String apiKey) {
		this.restTemplate = restTemplate;
		this.apiKey = apiKey;
	}

	@Override
	public String getPanDetails(pandto dto, HttpServletRequest request, HttpServletResponse response) {

		String APIURL = propertiesConfig.getPanApiURL();

		String requestUrl = request.getRequestURI().toString();

		int statusCode = response.getStatus();

		pandto panrequest = new pandto();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", apiKey); // Include API key directly without "Bearer" prefix

//		 String requestBody = "{\"number\": \"" + dto.getNumber() +
//		  "\", \"returnIndividualTaxComplianceInfo\": \"" +
//		  dto.getReturnIndividualTaxComplianceInfo() + "\"}";

		Gson gson = new Gson();

		String requestBodyJson = gson.toJson(dto);

		panrequest.setNumber(dto.getNumber());
		panrequest.setReturnIndividualTaxComplianceInfo(dto.getReturnIndividualTaxComplianceInfo());

		HttpEntity<String> entity = new HttpEntity(requestBodyJson, headers);

		ApiLog apiLog = new ApiLog();
		apiLog.setUrl(requestUrl);
		apiLog.setRequestBody(requestBodyJson);
		apiLog.setStatusCode(statusCode);
		try {
			String response1 = restTemplate.postForObject(APIURL, entity, String.class);
			apiLog.setResponseBody(response1);
			apiLog.setStatusCode(HttpStatus.OK.value());
			return response1;
		} catch (HttpClientErrorException.BadRequest e) {
			String errorMessage = e.getResponseBodyAsString();

			apiLog.setResponseBody(errorMessage);
			apiLog.setStatusCode(e.getStatusCode().value());
			;
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
						} else {

							throw e;
						}

					}

				}
			}
		} catch (HttpClientErrorException.Conflict e) {
			String errorMessage = e.getResponseBodyAsString();
			logger.error("Error Response: {}", errorMessage);
			if (errorMessage.contains("Upstream Down")) {
				throw new UpstreamDownException("Upstream Down");
			} else {
				throw e;
			}
		} catch (HttpClientErrorException.Unauthorized e) {
			String errorMessage = e.getResponseBodyAsString();
			logger.error("Error Response: {}", errorMessage);
			if (errorMessage.contains("[no body]")) {
				throw new UnauthorizedException("[no body]");
			} else {
				throw e;
			}
		} finally {
			apiLogRepository.save(apiLog);
		}

	}
}
