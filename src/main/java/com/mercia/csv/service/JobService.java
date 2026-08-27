package com.mercia.csv.service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mercia.csv.entities.JobAudit;
import com.mercia.csv.entities.StatusEnum;
import com.mercia.csv.repository.JobRepository;

@Service
public class JobService {

	private final JobRepository jobRepository;
	private CsvParserService csvParserService;

    public JobService(JobRepository jobRepository,CsvParserService csvParserService) {
        this.jobRepository = jobRepository;
        this.csvParserService = csvParserService;
    }
     public JobAudit createJob()
     {
    	 JobAudit job = new JobAudit();
    	 job.setStatus(StatusEnum.IN_PROGRESS);
    	 job.setCreatedAt(LocalDateTime.now());
    	 jobRepository.save(job);
    	
    	 return job;
     }
     
    @Async
     public void processJob(MultipartFile file,Long jobId)
     {
    	 JobAudit job = jobRepository.findById(jobId).orElseThrow();
    	 try {
    		
    			 int failed_records = csvParserService.processCSV(job, file.getInputStream());
    			
    			job.setFile_name(file.getOriginalFilename());
    			job.setEndedAt(LocalDateTime.now());
    			job.setStatus(StatusEnum.SUCCCESS);
    			job.setFailedCount(failed_records);
    			job.setSuccessCount(501-failed_records);
    			job.setDuration(Duration.between(job.getCreatedAt(), job.getEndedAt()).toSeconds());
    			jobRepository.save(job);
    	 }
    	 catch(Exception e)
    	 {
    		 System.out.println(e.getMessage());
    		 job.setStatus(StatusEnum.FAILED);
    		
    		 
    	 }
    	 
     }
     
}
