package com.mercia.csv.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mercia.csv.dto.UploadResponseDto;
import com.mercia.csv.entities.JobAudit;
import com.mercia.csv.entities.StatusEnum;
import com.mercia.csv.service.JobService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name="CSV Processor")
public class UploadController {

//	private CsvParserService csvParserService;
	
	private UploadResponseDto uploadResponseDto;
	

	private JobService jobService;
	
	private final CsvFileValidator csvFileValidator;
	
	public UploadController(UploadResponseDto uploadResponseDto,JobService jobService,CsvFileValidator csvFileValidator)
	{
		
		this.uploadResponseDto = uploadResponseDto;
		this.jobService = jobService;
		this.csvFileValidator = csvFileValidator;
	}
	
	@PostMapping(value= "/upload")
	public ResponseEntity<UploadResponseDto> uploadCSV(@RequestParam("file") MultipartFile file) {
		
		JobAudit job = jobService.createJob();
		try
		{ 
			// file validation
			if (!csvFileValidator.isValid(file))
			   throw new RuntimeException("Invalid csv file");
		
		}
		catch(RuntimeException e)
		{
			System.out.println(e.getMessage());
			 
				uploadResponseDto.setJobId(job.getId());
				job.setStatus(StatusEnum.FAILED);
				uploadResponseDto.setMessage("File not accepted");
				return ResponseEntity.badRequest().body(uploadResponseDto);
		}
	//	System.out.println(job.getId());
	   jobService.processJob(file, job.getId());
		uploadResponseDto.setJobId(job.getId());
		
		uploadResponseDto.setMessage("File accepted for background processing.");
		return ResponseEntity.ok().body(uploadResponseDto);
		
	}
	
	
	
}
