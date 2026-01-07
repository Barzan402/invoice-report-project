package com.vgb;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/*
 * Represents leased equipment, which is a type of equipment
 * and has a start and end date for the lease.
 */
public class LeasedEquipment extends Equipment {
	private LocalDate startDate; //Lease start date
	private LocalDate endDate; //Lease end date

	/*
	 * Constructor to create a leased equipment item
	 * which shows the unique ID for the leased equipment 
	 * and also the name, model number, retail price and the dates
	 * from when the lease begins and ends.
	 */
    
	public LeasedEquipment(UUID uuid, String name, String modelNumber, double retailCost, LocalDate startDate, LocalDate endDate) {
		super(uuid, name, modelNumber, retailCost); // Calls the equipment constructor
		this.startDate = startDate;
		this.endDate = endDate;
	}

	/*
	 * Calculates the cost of leasing the equipment
	 * and the cost is based on how long the lease is in days
	 * and amortized over 5 years with 50% markup.
	 */
	@Override
	public double calculateCost() {
		LocalDateTime start = startDate.atStartOfDay();
		LocalDateTime end = endDate.atStartOfDay();
		long leaseDays = Duration.between(start, end).toDays() + 1;
		double leaseYears = leaseDays / 365.0;
		return (leaseYears / 5) * getRetailCost() * 1.5;
	}

	/*
	 * Calculates the tax for leased equipment based on the total lease cost
	 * it will have no tax if it cost < $5000
	 * it will have $500 tax if the cost is between $5000 and $12500
	 */
	@Override
	public double calculateTax() {
		double retailPrice = getRetailCost();
	    
	    if (retailPrice < 5000) {
	    	return 0.0;
	    } else if (retailPrice < 12500) {
	    	return 500.0;
	    } else {
	    	return 1500.0;
	    }
	}

	// Gets the Item type for the Leased Equipment
	@Override
	public String getItemType() {
		return "(Lease)";
	}
	
	// Returns a string of the leased equipment
	@Override
	public String toString() {
		return "Equipment ID: " + getUuid() + " " + getItemType() + "\n" +
				"Name: " + getName() + "\n" +
				"Model: " + getModelNumber() + "\n" +
				"Start Date: " + startDate + "\n" +
				"End Date: " + endDate + "\n" +
				"Lease Cost: $" + String.format("%.2f", calculateCost()) + "\n" +
				String.format("%35s %10s %11s", "", "Tax", "Total") + "\n" +
				String.format("%36s $%8.2f $%10.2f", "", calculateTax(), calculateCost());
    }
}
