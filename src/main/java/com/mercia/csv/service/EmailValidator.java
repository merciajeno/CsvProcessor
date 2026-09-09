package com.mercia.csv.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.mercia.csv.entities.UserRecord;

@Component
public class EmailValidator implements UserRecordValidationRule{

	private static final String EMAIL_REGEX = 
	        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
	 private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
	 
	 
	@Override
	public boolean validate(String email) {
		// TODO Auto-generated method stub
		 if (email == null) {
	            return false;
	        }
	        Matcher matcher = EMAIL_PATTERN.matcher(email);
	        return matcher.matches();
	}

}
