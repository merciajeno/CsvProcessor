package com.mercia.csv.service;

import org.springframework.stereotype.Component;

@Component
public class ZipCodeValidator implements UserRecordValidationRule{

	@Override
	public boolean validate(String zipcode) {
		// TODO Auto-generated method stub
		 if(zipcode.length()!=5)return false;
		 return zipcode.matches("[0-9]+");
	}
	

}
