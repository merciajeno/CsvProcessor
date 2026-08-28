package com.mercia.csv.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mercia.csv.entities.JobAudit;
import com.mercia.csv.entities.JobError;
import com.mercia.csv.entities.StatusEnum;
import com.mercia.csv.repository.JobRepository;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/job")
@Tag(name="CSV Processor")
public class JobController {

	@Autowired
	private JobRepository jobRepo;
	
	@GetMapping("/{jobId}")
	public List<JobError> getAudits(@PathVariable Long jobId)
	{
		 JobAudit job = jobRepo.findById(jobId)
		 .orElse(null);
		 if (job==null)return new ArrayList<>();
		 return job.getJobErrors();
	}
	
	@GetMapping("/status/{jobId}")
	public ResponseEntity<StatusEnum> getStatus(@PathVariable Long jobId)
	{
		 JobAudit job = jobRepo.findById(jobId)
				 .orElse(null);
		 if(job==null)
		 {
			 return ResponseEntity.notFound().build();
		 }
		 return ResponseEntity.ok().body(job.getStatus());
	}
}
