package com.vgb;

import java.util.UUID;

/**
 * Represents a material in the VGB system.
 * Materials are sold in units and have a price per unit.
 */
public class Material extends Item {
	private String unit;
	private double pricePerUnit;
	private int quantity;

	public Material(UUID uuid, String name, String unit, double pricePerUnit, int quantity) {
		super(uuid, name); // Calls the constructor of the Item class
		this.unit = unit;
		this.pricePerUnit = pricePerUnit;
		this.quantity = quantity;
	}
	
	public Material(UUID uuid, String name, String unit, double pricePerUnit) {
	    super(uuid, name);
	    this.unit = unit;
	    this.pricePerUnit = pricePerUnit;
	    this.quantity = 0; // default quantity, will be set later in readInvoiceItems()
	}

	
	public Material(Material other) {
		super(other.getUuid(), other.getName());
		this.unit = other.unit;
		this.pricePerUnit = other.pricePerUnit;
		this.quantity = other.quantity;
	}

	public String getUnit() {
		return unit;
	}

	public double getPricePerUnit() {
		return pricePerUnit;
	}
	
	public int getQuantity() {
		return quantity;
	}
	//Calculates the total cost of the material
	@Override
	public double calculateCost() {
		return pricePerUnit * quantity; 
	}
	// Calculates the tax on the material (7.15% sales tax)
	@Override
	public double calculateTax() {
		return TaxCalculator.calculateTax(calculateCost(), 0.0715);
	}
	// Gets the Item type for Material 
	@Override
	public String getItemType() {
		return "(Material)";
	}
	
	
	// String representation of the material 
	@Override
	public String toString() {
		return "\n" +
				"Material ID: " + getUuid() + " " + getItemType() + "\n" +
				"Name: " + getName() + "\n" +
				"Unit: " + unit + "\n" +
				"Price: $" + String.format("%.2f/Unit", pricePerUnit) + "\n" +
				"Quantity: " + quantity + "\n" +
				String.format("Cost: $%.2f\n", calculateCost()) + 
				String.format("%35s %10s %11s", "", "Tax", "Total") + "\n" +
				String.format("%36s $%8.2f $%10.2f", "", calculateTax(), calculateCost());
	}



	
	
}
