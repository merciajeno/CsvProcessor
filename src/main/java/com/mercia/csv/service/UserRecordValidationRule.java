package com.mercia.csv.service;

import org.springframework.stereotype.Component;

@Component
public interface UserRecordValidationRule {
    boolean validate(String validate);
}