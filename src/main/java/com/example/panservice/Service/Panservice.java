package com.example.panservice.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.panservice.Entity.pandto;
//import com.example.panservice.Response.Panresponse;


@Service
public interface Panservice {


	public Object getPanDetails(pandto dto);

}
