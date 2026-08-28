package com.mercia.csv;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.mock.web.MockMultipartFile;
import com.mercia.csv.controller.CsvFileValidator;


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
	    	//zipcode  is missing
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
