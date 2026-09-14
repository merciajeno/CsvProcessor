package com.mercia.csv.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import com.mercia.csv.dto.CsvResult;
import com.mercia.csv.entities.Address;
import com.mercia.csv.entities.JobAudit;
import com.mercia.csv.entities.JobError;
import com.mercia.csv.repository.JobErrorRepository;

@Service
public class CsvParserService {

	
	private final JobErrorRepository jobErrorRepo;
	
	private final UserService userService;
	
	private final AddressService addressService;
	
	private final UserRecordValidator validator;
	
	private final CSVFormat csvFormat;
	
	private final CsvResult csvResult;
	
	public CsvParserService(JobErrorRepository jobErrorRepo, UserRecordValidator validator, AddressService addressService, UserService userService, CSVFormat csvFormat, CsvResult csvResult) {
		this.jobErrorRepo = jobErrorRepo;
		this.userService = userService;
		this.validator = validator;
		this.addressService = addressService;
		this.csvFormat = csvFormat;
		this.csvResult = csvResult;
		
		// TODO Auto-generated constructor stub
	}
	
    public int totalRecords(BufferedReader reader)
    {
    	
    	CSVParser csv=null;
		try {
			csv = csvFormat.parse(reader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return csv.getRecords().size();
    	
    }
	public CsvResult processCSV(JobAudit job,InputStream fileInput)// here the csv is processed
	{
	
	int failed_records = 0;
	int totalRecords = 0;
		ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(fileInput)))
		{
			CSVParser csvParser = csvFormat.parse(reader);
	      //  totalRecords = csvParser.getRecords().size();
			totalRecords = 501;
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
			System.out.println(e1.getMessage());
			
		}
		executor.shutdown();
		csvResult.setFailedRecords(failed_records);
		csvResult.setSuccessRecords(totalRecords-failed_records);
	    return csvResult;
	}


	private int processCSVRecord(JobAudit job,CSVRecord record) {
		int failed_records=0;
		String zipcode = record.get("zipcode");//important field
		String email = record.get("email");// important field
		//System.out.println(zipcode);

		 JobError error = new JobError();
		 error.setJobAudit(job);
		 error.setRowNumber(record.getRecordNumber());
		try
		{
		if(!validator.isValidEmail(email))
		{
			throw new RuntimeException("Not a valid email");
		}
		  if(validator.isValidZipcode(zipcode))
		  {
			  Address address = addressService.getAddress(zipcode);
			
			 
			  userService.createIfNotExists(email, record, address, job);
			}
			
		
		  else
		  {
			
			  failed_records=1;
			
			 
			 error.setErrorMessage("Zipcode is invalid");
			 jobErrorRepo.save(error);
			 
		  }
		}
		catch(RuntimeException r)
		{
			failed_records=1;
			error.setErrorMessage(r.getMessage());
			jobErrorRepo.save(error);
		}
		  return failed_records;
	}
}
