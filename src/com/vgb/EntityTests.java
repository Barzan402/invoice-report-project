package com.vgb;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import java.time.Duration;
import java.time.LocalDate;

/**
 * This class contains JUnit tests to verify correct calculations for invoice items.
 */
public class EntityTests {
	
	private static final double TOLERANCE = 0.001;
	
	/**
	 * Test for purchasing equipment.
	 */
	@Test
	public void testEquipment() {
		// Data values for equipment
		UUID uuid = UUID.randomUUID();
		String name = "Backhoe 3000";
		String model = "BH30X2";
		double cost = 95125.00;
		
		// Creates a new Equipment object
		Equipment equipment = new Equipment(uuid, name, model, cost);
		
		double expectedCost = cost;
		double expectedTax = Math.round(cost * 0.0525 * 100.0) / 100.0; // Expected tax calculation (5.25% tax)
		
		assertEquals(expectedCost, equipment.calculateCost(), TOLERANCE); // Verifies cost
		assertEquals(expectedTax, equipment.calculateTax(), TOLERANCE); // Verifies tax
		
		String equipmentString = equipment.toString();
        assertTrue(equipmentString.contains(uuid.toString()));
        assertTrue(equipmentString.contains(name));
        assertTrue(equipmentString.contains(model));
        assertTrue(equipmentString.contains(String.valueOf(cost)));
	}

	/**
	 * Test for leasing equipment.
	 */
	@Test
	public void testLease() {
		// Data values for leased equipment
		UUID uuid = UUID.randomUUID();
		String name = "Bulldozer";
		String model = "XPXY4";
		double cost = 125500.00;
		LocalDate startDate = LocalDate.parse("2024-01-01");
		LocalDate endDate = LocalDate.parse("2026-06-01");
		
		// Creates a new LeasedEquipment object
		LeasedEquipment lease = new LeasedEquipment(uuid, name, model, cost, startDate, endDate);
		// Lease calculations
		long leaseDays = Duration.between(startDate.atStartOfDay(), endDate.atStartOfDay()).toDays() + 1;
		double leaseYears = leaseDays / 365.0;
		double expectedCost = (leaseYears / 5) * cost * 1.5;
		double expectedTax = lease.calculateTax();
		
		assertEquals(expectedCost, lease.calculateCost(), TOLERANCE); // Verifies lease cost
		assertEquals(expectedTax, lease.calculateTax(), TOLERANCE); // Verifies lease tax
		
		String leaseString = lease.toString();
		assertTrue(leaseString.contains(uuid.toString()));
		assertTrue(leaseString.contains(name));
		assertTrue(leaseString.contains(model));
		assertTrue(leaseString.contains(startDate.toString()));
		assertTrue(leaseString.contains(endDate.toString()));
		assertTrue(leaseString.contains(String.format("$%.2f", expectedCost)));
	}

	/**
	 * Test for renting equipment.
	 */
	@Test
	public void testRental() {
		// Data values for rented equipment
		UUID uuid = UUID.randomUUID();
		String name = "Skid-steer";
		String model = "S4325";
		double cost = 8500.00;
		int hours = 25;

		// Creates a new RentedEquipment object
		RentedEquipment rental = new RentedEquipment(uuid, name, model, cost, hours);
		
		double expectedCost = cost * 0.001 * hours; // Expected cost (0.1% of retail price per hour)
		double expectedTax = Math.round(expectedCost * 0.0438 * 100.0) / 100.0; // Expected tax (4.38% of rental cost)
		
		
		assertEquals(expectedCost, rental.calculateCost(), TOLERANCE); // Verifies rental cost
		assertEquals(expectedTax, rental.calculateTax(), TOLERANCE); // Verifies rental tax
		
		String rentalString = rental.toString();
		assertTrue(rentalString.contains(uuid.toString()));
		assertTrue(rentalString.contains(name));
		assertTrue(rentalString.contains(model));
		assertTrue(rentalString.contains(String.valueOf(hours)));
		assertTrue(rentalString.contains(String.valueOf(expectedCost)));

	}
	
	/**
	 * Test for purchasing materials.
	 */
	@Test
	public void testMaterial() {
		// Data values for material
		UUID uuid = UUID.randomUUID();
		String name = "Nails";
		String unit = "Box";
		double pricePerUnit = 9.99;
		int quantity = 31;
		
		// Creates a new Material object
		Material material = new Material(uuid, name, unit, pricePerUnit, quantity);
		
		double expectedCost = pricePerUnit * quantity; // PricePerUnit (9.99) * Quantity (31)
		double expectedTax = TaxCalculator.calculateTax(expectedCost, 0.0715); // Expected tax (7.15% of total cost)
		
		
		assertEquals(expectedCost, material.calculateCost(), TOLERANCE); // Verifies material cost
		assertEquals(expectedTax, material.calculateTax(), TOLERANCE); // Verifies material tax
		
		String materialString = material.toString();
		assertTrue(materialString.contains(uuid.toString()));
		assertTrue(materialString.contains(name));
		assertTrue(materialString.contains(unit));
		assertTrue(materialString.contains(String.valueOf(pricePerUnit)));
		assertTrue(materialString.contains(String.format("$%.2f", expectedCost)));
	}
	
	/**
	 * Test for contracts.
	 */
	@Test
	public void testContract() {
		// Data values for contract
		UUID uuid = UUID.randomUUID(); // Generates a random unique ID
		String name = "Foundation Pour"; // Name of the contract
		Company servicer = new Company(UUID.randomUUID(), null, "ABC Construction", null); // Servicing company with random name place holder
		double amount = 5000.00;
		Contract contract = new Contract(uuid, name, servicer, amount);
		
		// Expected cost for contracts is the defined amount
		assertEquals(5000.0, contract.calculateCost(), TOLERANCE); // Verifies contract cost is 0
		
		// Contracts have no tax
		assertEquals(TaxCalculator.calculateTax(amount, 0.0), contract.calculateTax(), TOLERANCE); // Verifies contract tax is 0
		
		// Represents Contract name appears in toString() output
		assertTrue(contract.toString().contains("Foundation Pour")); // Verified name shows in the string output
		
		String contractString = contract.toString();
		assertTrue(contractString.contains(uuid.toString()));
		assertTrue(contractString.contains(name));
		assertTrue(contractString.contains(servicer.getName()));
		assertTrue(contractString.contains(String.valueOf(amount)));
	}
}
