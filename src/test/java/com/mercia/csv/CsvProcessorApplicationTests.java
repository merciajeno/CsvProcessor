package com.mercia.csv;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.net.ssl.SSLEngineResult.Status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import com.mercia.csv.controller.UploadController;
import com.mercia.csv.dto.UploadResponseDto;

@SpringBootTest
//@WebMvcTest()
class CsvProcessorApplicationTests {

	@Autowired
	private UploadController uploadController;
	
	private MockMvc mock;
	
	@BeforeEach
	void setup() {
		this.mock = MockMvcBuilders.standaloneSetup(new UploadController()).build();
	}
	
	@Test
	void testWithOtherFormat() throws Exception {

	    MockMultipartFile file = new MockMultipartFile(
	            "file",
	            "test.txt",
	            "text/plain",
	            "Sample content".getBytes()
	    );
        
	    mock.perform(
	            MockMvcRequestBuilders
	                    .multipart("/api/v1/users/upload")
	                    .file(file)
	                    .with(request -> {
	                        request.setMethod("PUT");
	                        return request;
	                    })
	    )
	    .andExpect(status().is(405));

	    System.out.println("Not right format");
	}

	
	
}
