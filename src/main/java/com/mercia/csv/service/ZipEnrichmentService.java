package com.mercia.csv.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.benmanes.caffeine.cache.Cache;

@Service
public class ZipEnrichmentService {

	private final AddressPersistService addressService;
	
	private final  Cache<String, String> cache ;
	
	private final  WebClient webClient ;


	ZipEnrichmentService(AddressPersistService addressService, Cache<String,String> cache, WebClient webClient) {
	
		this.addressService = addressService;
		this.cache = cache;
		this.webClient = webClient;
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
