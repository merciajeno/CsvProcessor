package com.mercia.csv.dto;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Getter
@Setter
@NoArgsConstructor
public class CsvResult {

	private int successRecords;
	private int failedRecords;
	@Override
	public String toString() {
		return "CsvResult [successRecords=" + successRecords + ", failedRecords=" + failedRecords + "]";
	}
	
	
}
