package com.vgb;

import java.util.UUID;
/**
 * This class represents the company 
 * with a unique ID, a contact person 
 * a name and an address
 */
public class Company {
	private int companyId;
	private UUID uuid;
	private Person contact;
	private String name;
	private Address address;
	
	/*
	 * Constructor to create a company object
	 * it has the unique id for the company
	 * the contact person for the company
	 * along with the name of the company
	 * and its address
	 */
	
	public Company(int companyId, UUID uuid, Person contact, String name, Address address) {
		this.companyId = companyId;
		this.uuid = uuid;
		this.contact = contact;
		this.name = name;
		this.address = address;
	}
	
	public Company(UUID uuid, Person contact, String name, Address address) {
		this.uuid = uuid;
		this.contact = contact;
		this.name = name;
		this.address = address;
	}
	// Getter methods to get the company details
	public int getCompanyId() {
		return companyId;
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public Person getContact() {
		return contact;
	}
	
	public String getName() {
		return name;
	}
	
	public Address getAddress() {
		return address;
	}
	// Setter methofs to update the company details
	public void setContact(Person contact) {
		this.contact = contact;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setAddress(Address address) {
		this.address = address;
	}
	/*
	 * this converts the company details so that it is readable in a string format
	 */
	@Override
	public String toString() {
		return "Company{" + uuid + "\n" + 
				"Contact: " + contact + "\n" + 
				"Name: " + name + "\n" + 
				"Address: " + address + "\n}";
		}
	}
