package com.mercia.csv.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserRecord {

	    public UserRecord(String firstName, String lastName, String phone1, String phone2, String email, String web) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone1 = phone1;
		this.phone2 = phone2;
		this.email = email;
		this.web = web;
	}

		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String firstName;
	    private String lastName;

	    private String phone1;
	    private String phone2;

	    private String email;
	    private String web;

	    @ManyToOne
	    @JoinColumn(name = "zipcode")
	    @JsonBackReference
	    private Address address;
	    
	    @ManyToOne
	    @JoinColumn(name="job_id")
	    @JsonBackReference
	    private JobAudit jobAudit;
}
