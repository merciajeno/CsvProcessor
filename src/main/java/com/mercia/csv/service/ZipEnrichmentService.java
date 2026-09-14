package com.mercia.csv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.benmanes.caffeine.cache.Cache;
import com.mercia.csv.config.ApplicationConfiguration;

@Service
public class ZipEnrichmentService {

	private final AddressPersistService addressService;
	
	@Autowired
	private  Cache<String, String> cache ;
	
	@Autowired
	private  WebClient webClient ;


	ZipEnrichmentService( AddressPersistService addressService) {
	
		this.addressService = addressService;
	}

	public void zipDetails(String zipcode) {

	    String cached = cache.getIfPresent(zipcode);

	    if (cached != null) {
	        return;
	    }

	    String result = webClient.get()
	            .uri("/{postal-code}", zipcode)
	            .retrieve()
	            .bodyToMono(String.class)
	            .block();

	    try {


	    	addressService.saveAddress(result, zipcode, cache);

	    } catch (JsonProcessingException e) {
	        throw new RuntimeException(e);
	    }
	}
}
