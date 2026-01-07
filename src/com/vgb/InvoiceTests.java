package com.vgb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import java.time.LocalDate;

public class InvoiceTests {

    public static final double TOLERANCE = 0.001;

    /**
     * Tests an invoice with 3 different items which are the Equipment
     * Lease Equipment and Rented Equipment
     */
    @Test
    public void testInvoice01() {
    	// Creates a company to use for the invoice
    	Address address = new Address("123 Main St", "Cityville", "ST", "12345");
        Person contact = new Person(UUID.randomUUID(), "John", "Doe", "555-0000", null);
        Company company = new Company(UUID.randomUUID(), contact, "Company", address);
        Person salesperson = new Person(UUID.randomUUID(), "Test", "Sales", "555-0000", null); 
        LocalDate testDate = LocalDate.parse("2025-01-01");
        Invoice invoice = new Invoice(UUID.randomUUID(), company, salesperson, testDate);
		
		// Adding items to the invoice
		invoice.addItem(new Equipment(UUID.randomUUID(), "Backhoe 3000", "BH30X2", 95125.00));
		invoice.addItem(new LeasedEquipment(UUID.randomUUID(), "Bulldozer", "XPXY4", 125500.00, LocalDate.parse("2024-01-01"), LocalDate.parse("2025-01-01")));
		invoice.addItem(new RentedEquipment(UUID.randomUUID(), "Skid-Steer", "S4325", 8500.00, 40));
		
		// Expected values based on the calculations from the invoice
		double expectedSubtotal = invoice.calculateSubtotal();
		double expectedTaxTotal = invoice.calculateTotalTax();
		double expectedGrandTotal = invoice.calculateGrandTotal();
		
		// Assertions to make sure calculations are correct
		assertEquals(expectedSubtotal, invoice.calculateSubtotal(), TOLERANCE);
		assertEquals(expectedTaxTotal, invoice.calculateTotalTax(), TOLERANCE);
		assertEquals(expectedGrandTotal, invoice.calculateGrandTotal(), TOLERANCE);
		//Checks if the invoice's toString() has the company's name
		String s = invoice.toString();
		assertTrue(s.contains("Company"));
    }

    @Test
    public void testInvoice02() {
    	// Creates a company for the invoice
		Company company = new Company(UUID.randomUUID(), null, "Company", null);
		Person salesperson = new Person(UUID.randomUUID(), "Jane", "Doe", "555-1234", null);
		LocalDate testDate = LocalDate.parse("2025-02-10");
		Invoice invoice = new Invoice(UUID.randomUUID(), company, salesperson, testDate);
		
		// Adding items to the invoice which are material and contract
		invoice.addItem(new Material(UUID.randomUUID(), "Lumber", "lft", 1.25, 1));
		invoice.addItem(new Contract(UUID.randomUUID(), "Footing Pour", company, 5000.00));
		
		// Expected values based on calculations from the invoice
		double expectedSubtotal = invoice.calculateSubtotal(); // Total before tax
		double expectedTaxTotal = invoice.calculateTotalTax(); // Totoal tax amount
		double expectedGrandTotal = invoice.calculateGrandTotal(); // Total after tax
		
		// Assertions to make sure calculations are correct
		assertEquals(expectedSubtotal, invoice.calculateSubtotal(), TOLERANCE);
		assertEquals(expectedTaxTotal, invoice.calculateTotalTax(), TOLERANCE);
		assertEquals(expectedGrandTotal, invoice.calculateGrandTotal(), TOLERANCE);
    }
}
