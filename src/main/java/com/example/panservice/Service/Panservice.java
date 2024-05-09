package com.example.panservice.Service;

import org.springframework.stereotype.Service;

import com.example.panservice.Entity.pandto;
import com.example.panservice.Response.Panresponse;


@Service
public interface Panservice {


	public Panresponse getPanDetails(pandto dto);

}
