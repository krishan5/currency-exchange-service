package com.kk.microservices.currencyexchangeservice.restController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
public class CircuitBreakerController {
	
	private Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class);
	
	/**
	 * @Retry annotation is of resilience4j so that if any error occur during execution of this method then
	 * same request will be retried according to configured "maxRetryAttempts" property in application.properties.
	 * "name" property of @Retry annotation is used to define its configuration in application.properties.
	 * Ex : resilience4j.retry.instances.sample-api.maxRetryAttempts=5 
	 *  where sample-api here is name mentioned in @Retry annotation.
	 * "fallbackMethod" property is used when all retry attempts are executed but error occurs all times, after that
	 * method name mentioned in fallbackMethod property will be executed as fallback strategy. 
	 */
	@GetMapping("/sample-api")
	@Retry(name = "sample-api", fallbackMethod = "hardCodedResponse")
	public String sampleApi() {
		logger.info("Sample api call recieved - Retry");
		ResponseEntity<String> response = new RestTemplate().getForEntity("http://localhost:8080/dummyurl", String.class);
		return response.getBody();
	}
	
	/**
	 * Its method name should be same as mentioned in @Retry(fallbackMethod) property which you can see in sampleApi() method. 
	 */
	public String hardCodedResponse(Exception ex) {
		return "fallback response";
	}
	
	@GetMapping("/sample-api-2")
	@CircuitBreaker(name = "default", fallbackMethod = "hardCodedResponse")
	public String sampleApi2() {
		logger.info("Sample api 2 call recieved - CircuitBreaker");
		ResponseEntity<String> response = new RestTemplate().getForEntity("http://localhost:8080/dummyurl", String.class);
		return response.getBody();
	}
	
	@GetMapping("/sample-api-3")
	@RateLimiter(name = "sample-api-3")
	public String sampleApi3() {
		logger.info("Sample api 3 call recieved - RateLimiter");
		return "sample-api-3";
	}
	
	@GetMapping("/sample-api-4")
	@RateLimiter(name = "sample-api-4")
	public String sampleApi4() {
		logger.info("Sample api 4 call recieved - RateLimiter");
		return "sample-api-4";
	}
}
