package com.example.panservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.panservice.Entity.pandto;
import com.example.panservice.Response.Panresponse;
import com.example.panservice.Service.Panservice;

@RestController
public class pancontroller {

	
	
	
	@Autowired
	Panservice panservice;
	

	
	

	@PostMapping("pan/fetch")
	public ResponseEntity<Panresponse>PanDetails(@RequestBody pandto dto)
	{
		Panresponse response=panservice.getPanDetails(dto);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
}
