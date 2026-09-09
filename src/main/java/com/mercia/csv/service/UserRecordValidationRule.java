package com.mercia.csv.service;

import org.springframework.stereotype.Component;

import com.mercia.csv.entities.UserRecord;

@Component
public interface UserRecordValidationRule {
    boolean validate(String validate);
}