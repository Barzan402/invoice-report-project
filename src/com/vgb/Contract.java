package com.vgb;

import java.util.UUID;
/*
 * Represents a contract, which is a type of invoice item
 */
public class Contract extends Item { 
	private Company servicer; // The company providing the contract servicer
	private double amount;
	
	// Constructor to create a contract with a unique ID, name and servicing company
	public Contract(UUID uuid, String name, Company servicer, double amount) {
		super(uuid, name);
		this.servicer = servicer;
		this.amount = amount;
	}
	
	// Returns the company that provides the service for the contract
	public Company getServicer() {
		return servicer;
	}
	public double getAmount() {
		return amount;
	}
	
	// Contracts do not have a default cost so it returns 0.0
	@Override
	public double calculateCost() {
		return amount;
	}
	
	// Contracts are not taxed so it will return 0.0
	@Override
	public double calculateTax() {
		return 0.0;
	}
	
	@Override
	public String getItemType() {
		return "(Contract)";
	}
	
	// Returns a simple representation of the contract details
	@Override
	public String toString() {
		return "Contract ID: " + getUuid() + " " + getItemType() + "\n" +
			"Name: " + getName() + "\n" +
			"Servicer: " + servicer.getName() + "\n" +
			String.format("%35s %10s %11s", "", "Tax", "Total") + "\n" +
			String.format("%36s $%8.2f $%10.2f", "", calculateTax(), calculateCost());
	}

}
