package com.mercia.csv.service;

import java.util.Optional;

import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import com.mercia.csv.entities.Address;
import com.mercia.csv.entities.JobAudit;
import com.mercia.csv.entities.UserRecord;
import com.mercia.csv.repository.UserRepository;
//service to persist the successful user record 
@Service
public class UserService {

    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public void createIfNotExists(
            String email,
            CSVRecord record,
            Address address,
            JobAudit job) {

        Optional<UserRecord> existingUser =
                userRepo.findByEmail(email);

        if (existingUser.isEmpty()) {

            UserRecord user = new UserRecord(
                    record.get("firstName"),
                    record.get("lastName"),
                    record.get("phone1"),
                    record.get("phone2"),
                    record.get("email"),
                    record.get("web")
            );

            user.setAddress(address);
            user.setJobAudit(job);

            userRepo.save(user);
        }
        else
        {
        	existingUser.get().setJobAudit(job);
        	userRepo.save(existingUser.get());
        }
    }
}