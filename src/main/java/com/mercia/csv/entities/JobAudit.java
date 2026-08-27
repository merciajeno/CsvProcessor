package com.mercia.csv.entities;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name="audit_job")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class JobAudit {

	@Id
	@SequenceGenerator(name="job_seq",initialValue = 10000,allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "job_seq")
	private Long id;
	private String file_name;
	private StatusEnum status;
	private int successCount;
	private int failedCount;
	private LocalDateTime createdAt;
	private LocalDateTime endedAt;
	private Long duration;
	
	@OneToMany(mappedBy="jobAudit")
	@JsonManagedReference
	private List<JobError> jobErrors = new ArrayList<>();
	
	@OneToMany(mappedBy="jobAudit")
	@JsonManagedReference
	private List<UserRecord> userRecords = new ArrayList<>();
} 
