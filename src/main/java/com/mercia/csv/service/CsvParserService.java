package com.mercia.csv.service;

import com.mercia.csv.entities.Address;
import com.mercia.csv.entities.JobAudit;
import com.mercia.csv.entities.JobError;
import com.mercia.csv.repository.JobErrorRepository;

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

@Service
public class CsvParserService {

//	@Autowired
//	private ZipEnrichmentService zipService;
//	
//	@Autowired
//	private UserRepository userRepo;
//	
//	@Autowired
//	private AddressRepository addressRepo;
//	
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
	//email regex
//	private static final String EMAIL_REGEX = 
//	        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
//	 private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
//	 
//	 public static boolean isValidEmail(String email) {
//	        if (email == null) {
//	            return false;
//	        }
//	        Matcher matcher = EMAIL_PATTERN.matcher(email);
//	        return matcher.matches();
//	    }
//
//	 public static boolean isValidZipcode(String zipcode)
//	 {
//		 if(zipcode.length()!=5)return false;
//		 return zipcode.matches("[0-9]+");
//	 }
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
//			CSVParser csvParser = CSVFormat.DEFAULT
//		    		.builder()
//		    		.setHeader()
//		    		.get()
//		    		.parse(reader);
			CSVParser csvParser = csvFormat.parse(reader);
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
		  {// for US zipcode
//			zipService.zipDetails(zipcode);
//			Address address = addressRepo.findById(zipcode)
//			        .orElseThrow(() ->
//			                new RuntimeException("Address not found: " + zipcode)
//			        );
			  Address address = addressService.getAddress(zipcode);
			
//			Optional<UserRecord> existingUser = userRepo.findByEmail(email);
//			if(existingUser.isEmpty())
//			{
//			    UserRecord user = new UserRecord(
//			            record.get("firstName"),
//			            record.get("lastName"),
//			            record.get("phone1"),
//			            record.get("phone2"),
//			            record.get("email"),
//			            record.get("web"));
//			    
//
//			    user.setAddress(address);
//			    user.setJobAudit(job);
//		       // address.addUser(user);
//			    
//			    userRepo.save(user);
//			}
			 
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
