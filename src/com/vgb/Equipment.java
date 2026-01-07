package com.vgb;

import java.util.UUID;

/**
 * Represents a piece of equipment that can be included in an invoice
 * and each quipment has a model number and retail price
 */
public class Equipment extends Item {
	private String modelNumber;
	private double retailCost;

	/*
	 * Constructor to create an equipment item
	 * which involves the Unique ID for the equipment
	 * the name, model number and retail price of it
	 */

	public Equipment(UUID uuid, String name, String modelNumber, double retailCost) {
		super(uuid, name);
		this.modelNumber = modelNumber;
		this.retailCost = retailCost;
	}
	
	public String getModelNumber() {
		return modelNumber;
	}

	public double getRetailCost() {
		return retailCost;
	}

	/*
	 * Gets the total cost of the equipment when purchased
	 * and the cost is just the retail price
	 */
	@Override
	public double calculateCost() {
		return retailCost;
	}

	
	// Calculates the tax (5.25%) and rounds it 2 decimal places
	@Override
	public double calculateTax() {
		return TaxCalculator.calculateTax(retailCost, 0.0525);
	}
	
	// Gets the item type of the equipment
	@Override
	public String getItemType() {
		return "(Purchase)";
	}

	// Returns a simple string which represents the equipment
	@Override
	public String toString() {
		return "Equipment ID: " + getUuid() + " " + getItemType() + "\n" +
				"Name: " + getName() + "\n" +
				"Model: " + getModelNumber() + "\n" +
				"Cost: $" + String.format("%.2f", getRetailCost()) + "\n" +
				String.format("%35s %10s %11s", "", "Tax", "Total") + "\n" +
				String.format("%36s $%8.2f $%10.2f", "", calculateTax(), calculateCost());
	}

}
