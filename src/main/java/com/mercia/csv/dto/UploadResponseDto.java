package com.mercia.csv.dto;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Getter
@Setter
@NoArgsConstructor
public class UploadResponseDto {

	private Long jobId;
	private String message;
	
	
}
