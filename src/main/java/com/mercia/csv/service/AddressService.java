package com.mercia.csv.service;

import org.springframework.stereotype.Service;

import com.mercia.csv.entities.Address;
import com.mercia.csv.repository.AddressRepository;

@Service
public class AddressService {

	private final ZipEnrichmentService zipService;
    private final AddressRepository addressRepo;

    public AddressService(
            ZipEnrichmentService zipService,
            AddressRepository addressRepo) {
        this.zipService = zipService;
        this.addressRepo = addressRepo;
    }

    public Address getAddress(String zipcode) {

        zipService.zipDetails(zipcode);

        return addressRepo.findById(zipcode)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Address not found: " + zipcode));
    }
}
