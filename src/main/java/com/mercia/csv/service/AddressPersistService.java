package com.mercia.csv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.mercia.csv.entities.Address;
import com.mercia.csv.repository.AddressRepository;
// to persist address record 
@Service
public class AddressPersistService {

	private final AddressRepository addressRepo;

	AddressPersistService(AddressRepository addressRepo) {
		this.addressRepo = addressRepo;
	}
	
	 public void saveAddress(String result,String zipcode,Cache<String, String> cache) throws JsonMappingException, JsonProcessingException
	    {
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
	    }
}
