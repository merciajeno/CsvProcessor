package com.mercia.csv.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class UserRecordValidator {

	private static final String EMAIL_REGEX = 
	        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
	 private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
	 
	 public static boolean isValidEmail(String email) {
	        if (email == null) {
	            return false;
	        }
	        Matcher matcher = EMAIL_PATTERN.matcher(email);
	        return matcher.matches();
	    }

	 public static boolean isValidZipcode(String zipcode)
	 {
		 if(zipcode.length()!=5)return false;
		 return zipcode.matches("[0-9]+");
	 }
	 

}
