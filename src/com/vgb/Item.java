package com.vgb;

import java.util.UUID;
/*
 * Represents an abstract item that can be inculded in an invoice
 * and this is a base class for all types of items.
 */
public abstract class Item {
	private UUID uuid;
	private String name;

	/*
	 * Constructor to set an item with a unique ID and name
	 * 
	 */
	public Item(UUID uuid, String name) {
		this.uuid = uuid;
		this.name = name;
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public String getName() {
		return name;
	}
	// Abstract method to calculate the cost of the item
	public abstract double calculateCost();
	
	// Abstract method to calculate the tax for the item
	public abstract double calculateTax();
	
	public double calculateGrandTotal() {
		return calculateCost() + calculateTax();
	}
	
	public abstract String getItemType();	
	
	// Returns a simple string representation of the item
	@Override
	public String toString() {
		return "Item ID: " + getUuid() + ", Name: " + getName();
	}

}
