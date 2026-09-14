package com.mercia.csv.service;

import org.springframework.stereotype.Service;
//validate the record based on email and zipcode
@Service
public class UserRecordValidator {

//	 
	private final  EmailValidator emailValidator;
	
	private final ZipCodeValidator zipValidator;

	UserRecordValidator(EmailValidator emailValidator, ZipCodeValidator zipValidator) {
		this.emailValidator = emailValidator;
		this.zipValidator = zipValidator;
	}
	
	 public  boolean isValidEmail(String email) {
	       return emailValidator.validate(email);
	    }

	 public  boolean isValidZipcode(String zipcode)
	 {
		 return zipValidator.validate(zipcode);
	 }
	 

}
