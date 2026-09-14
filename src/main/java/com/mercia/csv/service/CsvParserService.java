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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	@Autowired
	private CSVFormat csvFormat;
	
	public CsvParserService(JobErrorRepository jobErrorRepo, UserRecordValidator validator, AddressService addressService,UserService userService) {
		this.jobErrorRepo = jobErrorRepo;
		this.userService = userService;
		this.validator = validator;
		this.addressService = addressService;
		
		// TODO Auto-generated constructor stub
	}
	
	
	public int  getTotalRecords(InputStream file)
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(file));
		try {
			CSVParser csvParser = csvFormat.parse(reader);
			int total = 0;
			for (CSVRecord record : csvParser) {
                total++;
            }
			return total;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			
		}
		return 0;
	}
	public int processCSV(JobAudit job,InputStream fileInput)// here the csv is processed
	{
	
	int failed_records = 0;
		ExecutorService executor = Executors.newFixedThreadPool(4);
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(fileInput)))
		{
			CSVParser csvParser = csvFormat.parse(reader);
			
			
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
			System.out.println(e1.getMessage());
			
		}
		
	return failed_records;
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
			
			  failed_records++;
			
			 
			 error.setErrorMessage("Zipcode is invalid");
			 jobErrorRepo.save(error);
			 
		  }
		}
		catch(RuntimeException r)
		{
			failed_records++;
			error.setErrorMessage(r.getMessage());
			jobErrorRepo.save(error);
		}
		  return failed_records;
	}
}
