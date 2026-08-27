package com.mercia.csv.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mercia.csv.dto.UploadResponseDto;
import com.mercia.csv.entities.JobAudit;
import com.mercia.csv.repository.JobRepository;
import com.mercia.csv.service.CsvParserService;
import com.mercia.csv.service.JobService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name="CSV Processor")
public class UploadController {

//	private CsvParserService csvParserService;
	private UploadResponseDto uploadResponseDto;
	
	@Autowired
	private JobService jobService;
	
	
	public UploadController(UploadResponseDto uploadResponseDto)
	{
		
		this.uploadResponseDto = uploadResponseDto;
	}
	
	@PostMapping(value= "/upload")
	public ResponseEntity<UploadResponseDto> uploadCSV(@RequestParam("file") MultipartFile file) {
		// if no file is uploaded
		JobAudit job = jobService.createJob();
		try
		{
		
		if(file.isEmpty()) throw new RuntimeException("File not found");
		//System.out.println(file.getContentType());
		
		//if file is in csv
		if(!file.getContentType().equals("text/csv")) throw new RuntimeException("Not in csv format");
		
//		try {
//			System.out.println("File input");
//			csvParserService.processCSV( file.getInputStream());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		}
		catch(RuntimeException e)
		{
			System.out.println(e.getMessage());
		}
	   jobService.processJob(file, job.getId());
		uploadResponseDto.setJobId(job.getId());
		
		uploadResponseDto.setMessage("File accepted for background processing.");
		return ResponseEntity.ok().body(uploadResponseDto);
		
	}
	
	
	
}
