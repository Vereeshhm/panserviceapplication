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

		ApiLog apiLog = new ApiLog();
		String response1 = null;
		try {
			String APIURL = propertiesConfig.getPanApiURL();

			String requestUrl = request.getRequestURI().toString();

			pandto panrequest = new pandto();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			headers.set("Authorization", apiKey);

//		 String requestBody = "{\"number\": \"" + dto.getNumber() +
//		  "\", \"returnIndividualTaxComplianceInfo\": \"" +
//		  dto.getReturnIndividualTaxComplianceInfo() + "\"}";

			Gson gson = new Gson();

			String requestBodyJson = gson.toJson(dto);

			panrequest.setNumber(dto.getNumber());
			panrequest.setReturnIndividualTaxComplianceInfo(dto.getReturnIndividualTaxComplianceInfo());

			HttpEntity<String> entity = new HttpEntity(requestBodyJson, headers);

			apiLog.setUrl(requestUrl);
			apiLog.setRequestBody(requestBodyJson);

			response1 = restTemplate.postForObject(APIURL, entity, String.class);
			apiLog.setResponseBody(response1);
			apiLog.setStatusCode(HttpStatus.OK.value());
			return response1;
		} catch (HttpClientErrorException.TooManyRequests e) {
			// Handling Too Many Requests Exception specifically
			apiLog.setStatusCode(HttpStatus.TOO_MANY_REQUESTS.value());

			response1 = e.getResponseBodyAsString();
			System.out.println(response1 + "Response");
			apiLog.setResponseBodyAsJson("API rate limit exceeded");
		} catch (HttpClientErrorException.Unauthorized e) {
			// Handling Unauthorized Exception specifically
			apiLog.setStatusCode(HttpStatus.UNAUTHORIZED.value());

			response1 = e.getResponseBodyAsString();
			System.out.println(response1 + "Response");
			apiLog.setResponseBodyAsJson("No API key found in request");

		}

		catch (HttpClientErrorException e) {
			apiLog.setStatusCode(e.getStatusCode().value());

			response1 = e.getResponseBodyAsString();
			System.out.println(response1 + " Response ");
			apiLog.setResponseBody(response1);
		}

		catch (Exception e) {
			apiLog.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

			response1 = e.getMessage();
			apiLog.setResponseBody(response1);
		}

		finally {
			apiLogRepository.save(apiLog);
		}
		return response1;

	}
}
