package com.mercia.csv;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import com.mercia.csv.controller.UploadController;

@SpringBootTest
class CsvProcessorApplicationTests {

	@Autowired
	private UploadController uploadController;
	
	@Test
	void contextLoads() {
		
	}

}
