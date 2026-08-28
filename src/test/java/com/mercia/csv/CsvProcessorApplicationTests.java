package com.mercia.csv;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.mock.web.MockMultipartFile;
import com.mercia.csv.controller.CsvFileValidator;
import com.mercia.csv.service.CsvParserService;


@SpringBootTest
class CsvProcessorApplicationTests {

	 private final CsvFileValidator validator = new CsvFileValidator();
	 
	 private final CsvParserService csvParserService = new CsvParserService();

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
	    
	    @Test
	    void validEmail()
	    {
	    	String email="hello@cred.ai";
	    	assertTrue(csvParserService.isValidEmail(email));
	    }
	    
	    
	    @Test
	    void invalidEmail()
	    {
	    	String email="hello@cred";
	    	assertFalse(csvParserService.isValidEmail(email));
	    }
	    
	    @Test
	    void validZipcode()
	    {
	    	assertTrue(csvParserService.isValidZipcode("12345"));
	    }
	    
	    @Test
	    void invalidZipcode()
	    {
	    	assertFalse(csvParserService.isValidZipcode("@ui"));
	    }
}
