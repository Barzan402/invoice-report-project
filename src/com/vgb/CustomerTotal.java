package com.vgb;

/*
 * This class keeps track of a customers total
 * invoice information
 */
public class CustomerTotal {
	private Company customer; // Holds the company/customer object
	private int numInvoices; // Keeps count of how many invoices the customer has
	private double totalAmount; // Adds up the total dollar amount of all invoices

	// Constructor that sets the customer and starts off invoice count and total to zero
	public CustomerTotal(Company customer) {
		this.customer = customer;
		this.numInvoices = 0;
		this.totalAmount = 0.0;
	}
	
	// Adds a new invoice amount to the customers total
	public void addInvoice(double amount) {
		this.numInvoices++; // Increases invoice count by 1
		this.totalAmount += amount; // Adds the new invoice amount to the total
	}
	
	// Returns the customer
	public Company getCustomer() {
		return customer;
	}
	
	// Returns the number of invoices the customer has
	public int getNumInvoices() {
		return numInvoices;
	}
	
	// Returns the total amount of all invoices for the customer
	public double getTotalAmount() {
		return totalAmount;
	}
	
}
