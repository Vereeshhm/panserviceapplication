package com.example.panservice.Service;

import org.springframework.stereotype.Service;

import com.example.panservice.Entity.pandto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@Service
public interface Panservice {


	public String getPanDetails(pandto dto,HttpServletRequest request,
			HttpServletResponse response);

}
