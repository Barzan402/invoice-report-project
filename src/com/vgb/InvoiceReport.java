package com.vgb;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;

/*
 * Reads the data from the CSV files
 * also links it with the required fields
 */
public class InvoiceReport {
	public static void main(String[] args) {
		List<Person> customers = DataLoader.loadPersons();
		DataLoader.loadEmails(customers);
		List<Address> addresses = DataLoader.loadAddresses();
		List<Company> companies = DataLoader.loadCompanies(customers, addresses);
		List<Item> items = DataLoader.loadItems(companies);
		List<Invoice> invoices = DataLoader.loadInvoices(companies, customers);
		DataLoader.loadInvoiceItems(invoices, items);


		//Phase 7 Tables: Reports (Loads all the reports at the top of the output) 
		
		// 1. Invoices by Total
		// Creates a sorted list to store invoices sored by grand total
		InvoiceListByTotal byTotalList = new InvoiceListByTotal();
		for (Invoice inv : invoices) {
			byTotalList.addInvoice(inv); // Adds each invoice to the list, keeping them sorted from high to low total
		}
		System.out.println("+-------------------------------------------------------------------------+");
		System.out.println("| Invoices by Total                                                       |");
		System.out.println("+-------------------------------------------------------------------------+");
		System.out.printf("%-36s %-26s %10s\n", "Invoice", "Customer", "Total");
		for (int i = 0; i < byTotalList.size(); i++) {
			Invoice inv = byTotalList.get(i); // Gets each invoice from the sorted list
			System.out.printf("%-36s %-26s $%10.2f\n", 
				inv.getInvoiceId().toString(), // Prints the invoice UUID
				inv.getCustomer().getName(), // Prints the customer (company) name
				inv.calculateGrandTotal()); // Prints the total cost of the invoice
		}
		System.out.println(); // Blank line after the table
		
		// 2. Invoices by Customer
		// Creates a list to sort invoices alphabetically by customer name
		InvoiceListByCustomer byCustomerList = new InvoiceListByCustomer();
		for (Invoice inv : invoices) {
			byCustomerList.addInvoice(inv); // Adds each invoice, sorted by customer
		}
		System.out.println("+-------------------------------------------------------------------------+");
		System.out.println("| Invoices by Customer                                                    |");
		System.out.println("+-------------------------------------------------------------------------+");
		System.out.printf("%-36s %-26s %10s\n", "Invoice", "Customer", "Total");
		for (int i = 0; i < byCustomerList.size(); i++) {
			Invoice inv = byCustomerList.get(i); // Gets each invoice from the sorted list
			System.out.printf("%-36s %-26s $%10.2f\n", 
				inv.getInvoiceId().toString(), // Invoice UUID
				inv.getCustomer().getName(), // Customer (company) name
				inv.calculateGrandTotal()); // Invoice total
		}
		System.out.println(); // Blank line after table
		
		// 3. Customer Invoice Totals
		// Creats a list to store the customers sorted by the total invoice amount
		CustomerListByTotal customerList = new CustomerListByTotal();
		for (Company company : companies) {
			CustomerTotal ct = new CustomerTotal(company); // Creates a CustomerTotal object for each company
			for (Invoice inv : invoices) {
				if (inv.getCustomer().equals(company)) { // If the invoice belongs to the current company
					ct.addInvoice(inv.calculateGrandTotal()); // it adds this invoice's total to that company's total
				}
			}
			customerList.add(ct); // Adds the completed CustomerTotal to the sorted list
		}
		System.out.println("+-------------------------------------------------------------------------+");
		System.out.println("| Customer Invoice Totals                                                 |");
		System.out.println("+-------------------------------------------------------------------------+");
		System.out.printf("%-26s %-22s %10s\n", "Customer", "Number of Invoices", "Total");
		for (int i = 0; i < customerList.size(); i++) {
			CustomerTotal ct = customerList.get(i); // Gets each customer's invoice summary
			System.out.printf("%-26s %-22d $%10.2f\n", 
				ct.getCustomer().getName(), // Company name
				ct.getNumInvoices(), // Total number of invoices from company
				ct.getTotalAmount()); // Total dollar amount of all their invoices
		}
		System.out.println(); // Blank line after the table

		
		// SUMMARY (Prints the column headers for the table)
		System.out.println("Summary Report By Total");
		System.out.println("------------------------------------------------------------------------------------------------------------------");
		System.out.printf("| %-40s | %-20s | %10s | %10s | %13s |\n", "Invoice #", "Customer", "Num Items", "Tax", "Total");
		System.out.println("------------------------------------------------------------------------------------------------------------------");
		// Initalizes totals
		double totalTax = 0.0;
		double grandTotal = 0.0;
		int totalItems = 0;
		// Copys the invoice list and sorts it in order going down by Grand Total
		List<Invoice> sortedInvoices = new ArrayList<>(invoices);
		Collections.sort(sortedInvoices, new Comparator<Invoice>() {
			public int compare(Invoice i1, Invoice i2) {
				return Double.compare(i2.calculateGrandTotal(), i1.calculateGrandTotal());
			}
		});
		// Prints each invoice summary and calculates the totals with the correct rounding
		for (Invoice invoice : sortedInvoices) {
			double invTax   = Math.round(invoice.calculateTotalTax() * 100.0) / 100.0;
			double invTotal = Math.round(invoice.calculateGrandTotal() * 100.0) / 100.0;
			System.out.printf("| %-40s | %-20s | %10d | $%9.2f | $%12.2f |\n",
					invoice.getInvoiceId(),
					invoice.getCustomer().getName(),
					invoice.getItems().size(),
					invTax,
					invTotal);
			
			totalTax += invTax;
			grandTotal += invTotal;
			totalItems += invoice.getItems().size(); // Adds to the item count
		}
		
		// Prints totals row for the summary
		System.out.println("------------------------------------------------------------------------------------------------------------------");
		System.out.printf("| %63s %10d | $%9.2f | $%12.2f |\n", "TOTALS", totalItems, totalTax, grandTotal);
		System.out.println("------------------------------------------------------------------------------------------------------------------");

		// CUSTOMER (Prints the clumn headers for the customer summary)
		System.out.println("Company Invoice Summary Report");
		System.out.println("------------------------------------------------------------------");
		System.out.printf("| %-30s | %10s | %15s |\n", "Company", "# Invoices", "Grand Total");
		System.out.println("------------------------------------------------------------------");
		// Maps to hold company totals and counts
		Map<Company, Double> companyTotals = new LinkedHashMap<>();
		Map<Company, Integer> companyCounts = new LinkedHashMap<>();
		// Pre fill company maps with some default values
		for (Company company : companies) {
			companyTotals.put(company, 0.0);
			companyCounts.put(company, 0);
		}
		// Count invoices and adds totals for each company
		for (Invoice invoice : invoices) {
			Company company = invoice.getCustomer();
			companyTotals.put(company, companyTotals.get(company) + invoice.calculateGrandTotal());
			companyCounts.put(company, companyCounts.get(company) + 1);
		}
		// Print totals per company
		for (Company company : companies) {
        	System.out.printf("| %-30s | %10d | $%14.2f |\n",
					company.getName(),
					companyCounts.get(company),
					companyTotals.get(company));
		}
		// Print grand total for all companies
		System.out.println("-----------------------------------------------------------------");
		System.out.printf("| %30s | %10d | $%14.2f |\n", "TOTALS", invoices.size(), grandTotal);

		// DETAILED INVOICE REPORT ( Prints full details of each invoice using its toString())
		System.out.println("Detailed Invoice Report");
		System.out.println("----------------------------------------------");
		for (Invoice invoice : invoices) {
			System.out.print(invoice.toString()); 
		}
	}
}
