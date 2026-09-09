package com.mercia.csv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//validate the record based on email and zipcode
@Service
public class UserRecordValidator {

//	private static final String EMAIL_REGEX = 
//	        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
//	 private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
//	 
	@Autowired
	private  EmailValidator emailValidator;
	
	@Autowired
	private ZipCodeValidator zipValidator;
	
	 public  boolean isValidEmail(String email) {
	       return emailValidator.validate(email);
	    }

	 public  boolean isValidZipcode(String zipcode)
	 {
		 return zipValidator.validate(zipcode);
	 }
	 

}
