package com.mercia.csv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mercia.csv.entities.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, String>{

}
