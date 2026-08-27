package com.mercia.csv.service;

import com.mercia.csv.dto.ErrorDto;
import com.mercia.csv.dto.UploadResponseDto;
import com.mercia.csv.entities.Address;
import com.mercia.csv.entities.JobAudit;
import com.mercia.csv.entities.JobError;
import com.mercia.csv.entities.UserRecord;
import com.mercia.csv.repository.AddressRepository;
import com.mercia.csv.repository.JobErrorRepository;
import com.mercia.csv.repository.UserRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CsvParserService {

	private final ErrorDto errorDto;
	private ZipEnrichmentService zipService;
	private final UploadResponseDto uploadResponseDto;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private AddressRepository addressRepo;
	
	@Autowired
	private JobErrorRepository jobErrorRepo;
	
	public CsvParserService(ZipEnrichmentService zipService, ErrorDto errorDto,UploadResponseDto uploadResponseDto)
	{
		this.zipService=zipService;
		this.errorDto = errorDto;
		this.uploadResponseDto = uploadResponseDto;
	}
	
	
	public int processCSV(JobAudit job,InputStream fileInput)// here the csv is processed
	{
	
	int failed_records = 0;
		ExecutorService executor = Executors.newFixedThreadPool(4);
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(fileInput)))
		{
//			String line;
//			try {
//				reader.readLine();//to skip header
//				while ((line = reader.readLine()) != null) {
//				    System.out.println("Line: " + line);
//				    
//				}
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			CSVParser csvParser = CSVFormat.DEFAULT
		    		.builder()
		    		.setHeader()
		    		.get()
		    		.parse(reader);
			int count = 0;
			
			//int total_records = csvParser.getRecords().size();
			List<Future<Integer>> futures = new ArrayList<>();
			for(CSVRecord record:csvParser)
			{
				
				futures.add(executor.submit(() -> processCSVRecord(job, record)));
			   
			}
			for (Future<Integer> future : futures) {
			    try {
					failed_records += future.get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			errorDto.setErrorMessage(e1.getMessage());
			System.out.println(e1.getMessage());
			
		}
		
	return failed_records;
	}


	private int processCSVRecord(JobAudit job,CSVRecord record) {
		int failed_records=0;
		String zipcode = record.get("zipcode");
		//System.out.println(zipcode);
		  if(zipcode.length()==5)
		  {// for US zipcode
			zipService.zipDetails(zipcode);
			Address address = addressRepo.findById(zipcode)
			        .orElseThrow(() ->
			                new RuntimeException("Address not found: " + zipcode)
			        );
			String email = record.get("email");
			Optional<UserRecord> existingUser = userRepo.findByEmail(email);
			if(existingUser.isEmpty())
			{
			    UserRecord user = new UserRecord(
			            record.get("firstName"),
			            record.get("lastName"),
			            record.get("phone1"),
			            record.get("phone2"),
			            record.get("email"),
			            record.get("web"));
			    

			    user.setAddress(address);
		       // address.addUser(user);
			    
			    userRepo.save(user);
			}
			}
			
		
		  else
		  {
			// errorDto.setErrorMessage("Zipcode not found");
			  failed_records++;
			
			 JobError error = new JobError();
			 error.setJobAudit(job);
			 error.setRowNumber(record.getRecordNumber());
			 error.setErrorMessage("Zipcode is invalid");
			 jobErrorRepo.save(error);
			 
		  }
		  return failed_records;
	}
}
