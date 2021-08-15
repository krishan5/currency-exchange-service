package com.kk.microservices.currencyexchangeservice.restController;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.kk.microservices.currencyexchangeservice.entity.CurrencyExchange;

/**
 * In case we want to up two instances of same service i.e. two instances of currency-exchange-service,
 * then go to "Run Configuration"
 * you will see CurrencyExchangeServiceApplication configuration which spring boot app using already to start
 * its server when you run as spring boot app. where CurrencyExchangeServiceApplication is name of main class.
 * Just change its name to CurrencyExchangeServiceApplication8000 where 8000 is its own port number.
 * Then Duplicate it and a new run configuration will be created.
 * Give it name CurrencyExchangeServiceApplication8001 and add -Dserver.port=8001 in its VM arguments.
 * Now you just need to run CurrencyExchangeServiceApplication8001 configuration.
 * Two instances will be up with 8000 and 8001 port respectively of same application.
 */
@RestController
public class CurrencyExchangeController {
	
	//org.springframework.core.env.Environment object to get details like port on which application is running.
	@Autowired
	private Environment environment;
	
	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public CurrencyExchange retrieveExchangeValue(@PathVariable String from, @PathVariable String to) {
		CurrencyExchange currencyExchange = new CurrencyExchange(1000L, from, to, BigDecimal.valueOf(50));
		currencyExchange.setEnvironment( environment.getProperty("local.server.port") );
		return currencyExchange;
	}
	
}
