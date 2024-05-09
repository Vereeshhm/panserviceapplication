package com.example.panservice.Utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("file:src/main/resources/application.properties")
public class PropertiesConfig {

	

	@Value("${Pan.ApiURl}")                      
	private String PanApiURL;

	public String getPanApiURL() {
		return PanApiURL;
	}

	public void setPanApiURL(String panApiURL) {
		PanApiURL = panApiURL;
	} 
	
	
	
	
}
