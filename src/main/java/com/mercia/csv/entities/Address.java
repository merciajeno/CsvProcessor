package com.mercia.csv.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

	@Id
	private String zipcode;
	private String place;
	private String state;
	
	@OneToMany(mappedBy="address")
	@JsonManagedReference
	private List<UserRecord> users=new ArrayList<>();
	
	public void addUser(UserRecord user)
	{
		this.users.add(user);
	}
	
	public void removeUser(UserRecord user)
	{
		this.users.remove(user);
	}

	public Address(String zipcode, String place, String state) {
		// TODO Auto-generated constructor stub
		this.state = state;
		this.zipcode = zipcode;
		this.place = place;
	}
}
