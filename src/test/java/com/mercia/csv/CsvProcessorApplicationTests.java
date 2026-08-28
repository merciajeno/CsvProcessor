package com.mercia.csv;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

import javax.net.ssl.SSLEngineResult.Status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import com.mercia.csv.controller.CsvFileValidator;
import com.mercia.csv.controller.UploadController;
import com.mercia.csv.dto.UploadResponseDto;
import com.mercia.csv.entities.JobAudit;
import com.mercia.csv.repository.JobRepository;
import com.mercia.csv.service.JobService;

@SpringBootTest
class CsvProcessorApplicationTests {

	 private final CsvFileValidator validator = new CsvFileValidator();

	    @Test
	    void testWithOtherFormat() {

	        MockMultipartFile file = new MockMultipartFile(
	                "file",                 // parameter name
	                "test.txt",             // original filename
	                "text/plain",             // content type
	                "Sample".getBytes()     // file content
	        );

	        boolean result = validator.isValid(file);

	        assertFalse(result);
	    }
	
	    @Test
	    void testWithCsvFormat()
	    {
	    	String csv = """
	    	        firstName,lastName,zipcode,phone1,phone2,email,web
	    	        John,Doe,570001,9876543210,9123456780,john@example.com,https://example.com
	    	        """;
            MockMultipartFile file = new MockMultipartFile(
            		"file",
            		"users.csv",
            		"text/csv",
            		csv.getBytes(StandardCharsets.UTF_8));
            
            boolean result = validator.isValid(file);
            assertTrue(result);
            
	    			
	    }
	    
	    @Test
	    void testWithMissingColumns()
	    {
	    	String csv = """
	    	        firstName,lastName,phone1,phone2,email,web
	    	        John,Doe,9876543210,9123456780,john@example.com,https://example.com
	    	        """;
            MockMultipartFile file = new MockMultipartFile(
            		"file",
            		"users.csv",
            		"text/csv",
            		csv.getBytes(StandardCharsets.UTF_8));
            
            boolean result = validator.isValid(file);
            assertFalse(result);
            
	    }
}
