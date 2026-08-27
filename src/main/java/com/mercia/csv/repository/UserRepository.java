package com.mercia.csv.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mercia.csv.entities.UserRecord;

@Repository
public interface UserRepository extends JpaRepository<UserRecord, Long>{

	Optional<UserRecord> findByEmail(String email);
}
