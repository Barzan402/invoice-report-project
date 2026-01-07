package com.vgb;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

/*
 * This class represents a person with
 * a unique id, name, phone number
 * and an email or emails
 */
public class Person {
	private int personId;
	private UUID personUuid;
	private String firstName;
	private String lastName;
	private String phone;
	private List<String> emails; //List of emails
	
	/*
	 * Constructor to create a person object
	 * it has the unique id for the person
	 * and also the first and last name
	 * along with a phone number
	 * and also an email or emails
	 * also the emails can be empty if the person does not have an email
	 */
	
	public Person(UUID personUuid, String firstName, String lastName, String phone, List<String> emails) {
		this.personUuid = personUuid;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		// if the person has emails it will store them otherwise it creats an empty list
		if (emails != null) {
		    this.emails = emails;
		} else {
		    this.emails = new ArrayList<>();
		}
	}
	
	/*
	 * Constructor used for loading from the database
	 * and includes the database primary key personId
	 */
	public Person(int personId, UUID personUuid, String firstName, String lastName, String phone, List<String> emails) {
		this.personId = personId;
		this.personUuid = personUuid;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		if (emails != null) {
			this.emails = emails;
		} else {
			this.emails = new ArrayList<>();
		}
	}
	
	public int getPersonId() {
    	return personId;
    }

	public UUID getPersonUuid() {
		return personUuid;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getFullName() {
		return firstName + " " + lastName;
	}
	
	public String getPhone() {
		return phone;
	}
	// Gets the list of emails
	// and returns the list of email or emails
	public List<String> getEmails() {
		return emails;
	}
}
