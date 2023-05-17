package org.ojbc.booking.common.rest;

import org.apache.camel.Header;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class BookingExchangeErrorHandler {

	private RestTemplate restTemplate;
	
	private static final Log log = LogFactory.getLog( BookingExchangeErrorHandler.class );
	
	public void resendFailedBookingRecord(@Header("resendUrl") String url, @Header("bookingNumber") String bookingNumber)
	{
		String urlToPost = url + "/" + bookingNumber; 
		
		log.info("URL to post to: " + urlToPost);
				
		ResponseEntity<String> result = restTemplate.postForEntity(urlToPost, null, String.class);
		
		if (result.getStatusCode() == HttpStatus.OK)
		{
			log.info("Resend succesful");
		}
		else
		{
			log.info("Resend failed: " + result.getStatusCode().toString());
		}	
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
}
