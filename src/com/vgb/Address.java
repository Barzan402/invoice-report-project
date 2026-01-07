package com.vgb;

/**
 * This class represents the address 
 * with a street, city, state and zip code
 */

public class Address {
	// The variables to store the address details
	private int addressId;
	private String street;
	private String city;
	private String state;
	private String zip;

	/**
	 * Constructor to create an address object
	 * Shows the street name and number
	 * the city name
	 * the state name but abbreviated
	 * and the zip code 
	 */
	
	public Address(int addressId, String street, String city, String state, String zip) {
	    this.addressId = addressId;
	    this.street = street;
	    this.city = city;
	    this.state = state;
	    this.zip = zip;
	}
	
	public Address(String street, String city, String state, String zip) {
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
		
	}
	// The getter methods to get the address data/information
	public int getAddressId() {
		return addressId;
	}
	
	public String getStreet() {
		return street;
	}
	
	public String getCity() {
		return city;
	}
	
	public String getState() {
		return state;
	}
	
	public String getZip() {
		return zip;
	}
	
	// Setter methods to update the details
	public void setStreet(String street) {
		this.street = street;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public void setZip(String zip) {
		this.zip = zip;
	}
	/*
	 * Converts the address into a readable string
	 * and formatted contaning the full address
	 */
	@Override
	public String toString() {
		return street + ", " + city + ", " + state + " " + zip;
		}
	}
