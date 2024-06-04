package com.example.panservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.panservice.Entity.pandto;
import com.example.panservice.Service.Panservice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class pancontroller {

	@Autowired
	Panservice panservice;

	@PostMapping("pan/fetch")
	public String PanDetails(@RequestBody pandto dto, HttpServletRequest request, HttpServletResponse response) {

		return panservice.getPanDetails(dto, request, response);
	}

}
