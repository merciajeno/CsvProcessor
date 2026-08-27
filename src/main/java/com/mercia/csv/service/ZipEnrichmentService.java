package com.mercia.csv.service;

import java.util.concurrent.TimeUnit;


import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.mercia.csv.entities.Address;
import com.mercia.csv.repository.AddressRepository;

import reactor.core.publisher.Mono;

@Service
public class ZipEnrichmentService {

	private final AddressRepository addressRepo;
//	private final RestClient restClient = RestClient.builder()
//            .baseUrl("https://api.zippopotam.us/us")
//            .build();
//	
	private final Cache<String, String> cache = Caffeine.newBuilder()
            .maximumSize(200)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();
	private final WebClient webClient = WebClient.builder()
			.baseUrl("https://api.zippopotam.us/us").build();

	ZipEnrichmentService(AddressRepository addressRepo) {
		this.addressRepo = addressRepo;
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
	        ObjectMapper objectMapper = new ObjectMapper();

	        JsonNode json = objectMapper.readTree(result);

	        String state = json.get("places")
	                .get(0)
	                .get("state")
	                .asText();

	        String place = json.get("places")
	                .get(0)
	                .get("place name")
	                .asText();
            
	        cache.put(zipcode, place);

	        addressRepo.save(
	                new Address(zipcode, place, state)
	        );

	    } catch (JsonProcessingException e) {
	        throw new RuntimeException(e);
	    }
	}
}
