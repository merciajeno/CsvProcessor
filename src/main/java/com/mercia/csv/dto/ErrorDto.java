package com.mercia.csv.dto;

import org.springframework.stereotype.Component;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Component
public class ErrorDto {

	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private int rowNumber;
	private String errorMessage;
	
}
