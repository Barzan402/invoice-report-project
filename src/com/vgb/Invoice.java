package com.vgb;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.time.LocalDate;


/*
 * Represents an invoice in the system
 * and an invoice contains an ID, a customer and a list of items
 */
public class Invoice {
	private UUID invoiceId;
	private Company customer;
	private Person salesperson;
	private LocalDate date;
	private List<Item> items;

	/*
	 * Constructor to create an invoice for a customer
	 * and generates a random invoice ID and formats an empty list of items
	 */
	
	public Invoice(UUID invoiceId, Company customer, Person salesperson, LocalDate date) {
		this.invoiceId = invoiceId;
		this.customer = customer;
		this.salesperson = salesperson;
		this.date = date;
		this.items = new ArrayList<>();
	}
	
	// Adds an item to the invoice
	public void addItem(Item item) {
		items.add(item);
	}

	// Calculates the subtotal (amount of the item costs) before tax
	public double calculateSubtotal() {
		double subtotal = 0;
		for (Item item : items) {
			subtotal += item.calculateCost(); 
		}
		return subtotal;
	}
	
	// Calculates the total tax for all items in the invoice
	public double calculateTotalTax() {
		double totalTax = 0;
		for (Item item : items) {
			totalTax += item.calculateTax();
		}
		return totalTax;
	}
	
	// Calculates the final amount from the subtotal and the tax
	public double calculateGrandTotal() {
		return calculateSubtotal() + calculateTotalTax();
	}
	
	public UUID getInvoiceId() {
		return this.invoiceId;
	}
	
	public Company getCustomer() {
		return this.customer;
	}
	
	
	public Person getSalesperson() {
		return this.salesperson;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public List<Item> getItems() {
		return this.items;
	}
    
    // method that builds a string of all items in the invoice
    private String itemsToString() {
		String itemsString = "";
		for (Item item : items) {
			itemsString += item.toString() + "\n";
		}
		return itemsString;
    }
	
	/*
	 * Returns a simple string containing
	 * all the invoice information needed
	 * and also the totals with the correct
	 * calculations.
	 */
    @Override
    public String toString() {
    	return "Invoice #" + invoiceId + "\n" +
    		"Company: " + customer.getName() + "\n" +
    		"Date: " + date + "\n" + 
    		"----------------------------------------------\n" +
    		"Customer: " + customer.getContact().getFirstName() + " " + customer.getContact().getLastName() +
    		" (" + customer.getContact().getPersonUuid() + ")\n" +
    		"Email: " + customer.getContact().getEmails() + "\n" +
    		"\n" +
    		"Salesperson: " + salesperson.getFirstName() + " " + salesperson.getLastName() +
    		" (" + salesperson.getPersonUuid() + ")\n" +
    		"Email: " + salesperson.getEmails() + "\n" +
			
    		"Address: " + customer.getAddress().toString() + "\n\n" +
    		"Items (" + items.size() + "):\n" +
    		itemsToString() +
    		String.format("Subtotal:     $%10.2f\n", calculateSubtotal()) +
			String.format("Total Tax:    $%10.2f\n", calculateTotalTax()) +
			String.format("Grand Total:  $%10.2f\n", calculateGrandTotal()) +
			"----------------------------------------------\n";
    }

}

