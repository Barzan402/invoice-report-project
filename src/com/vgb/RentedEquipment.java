package com.vgb;

import java.util.UUID;
/*
 * Represents equipment that is rented by the hour
 * and the rental cost is based on the retail price
 * and the number of hours rented.
 */
public class RentedEquipment extends Equipment {
	private double hoursRented; 
	
	/*
	 * Constructor for rentened equipment which has the
	 * unique ID the name of the equipment the model number 
	 * and also the retail cost of the equipment
	 */
	public RentedEquipment(UUID uuid, String name, String modelNumber, double retailCost, double hoursRented) {
		super(uuid, name, modelNumber, retailCost);
		this.hoursRented = hoursRented;
	}
	/*
	 * Calculates the total rental cost 
	 * and the cost per hour is 0.1% of the retail price
	 */
	@Override
	public double calculateCost() {
		return getRetailCost() * 0.001 * hoursRented;
	}
	/*
	 * Calculates the sales tax on the rental
	 * the tax rate is 4.38% of the rental cost
	 */
	@Override
	public double calculateTax() {
		return TaxCalculator.calculateTax(calculateCost(), 0.0438);
	}
	
	// Gets the item type for a Rented Equipment
    @Override
    public String getItemType() {
        return "(Rental)";
    }
    // Returns a string representation of the rented equipment
    @Override
    public String toString() {
    	return "Equipment ID: " + getUuid() + " " + getItemType() + "\n" +
				"Name: " + getName() + "\n" +
				"Model: " + getModelNumber() + "\n" +
				"Hours Rented: " + String.format("%.2f", hoursRented) + "\n" +
				"Rental Cost: $" + String.format("%.2f", calculateCost()) + "\n" +
				String.format("%35s %10s %11s", "", "Tax", "Total") + "\n" +
				String.format("%36s $%8.2f $%10.2f", "", calculateTax(), calculateCost());
    }

}