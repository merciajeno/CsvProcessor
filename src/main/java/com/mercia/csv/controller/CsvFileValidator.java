package com.mercia.csv.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


@Component
public class CsvFileValidator {

	private final List<String> validHeaders = List.of(
	        "email",
	        "firstName",
	        "lastName",
	        "phone1",
	        "phone2",
	        "zipcode",
	        "web"
	);

	public boolean isValid(MultipartFile file) {

	    // Basic file validation first
	    if (file == null || file.isEmpty()) {
	        return false;
	    }

	    if (!"text/csv".equals(file.getContentType())) {
	        return false;
	    }

	    try (BufferedReader br = new BufferedReader(
	            new InputStreamReader(file.getInputStream()))) {

	        String headerLine = br.readLine();

	        if (headerLine == null) {
	            return false;
	        }

	        String[] headers = headerLine.split(",");

	        List<String> actualHeaders = Arrays.stream(headers)
	                .map(String::trim)
	                .toList();

	        System.out.println("Headers: " + actualHeaders);

	        // Order does NOT matter
	        return actualHeaders.size() == validHeaders.size()
	                && actualHeaders.containsAll(validHeaders);

	    } catch (IOException e) {
	        return false;
	    }
	}

}

