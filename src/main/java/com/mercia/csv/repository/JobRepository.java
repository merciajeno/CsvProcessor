package com.mercia.csv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mercia.csv.entities.JobAudit;

@Repository
public interface JobRepository extends JpaRepository<JobAudit, Long>{

}
