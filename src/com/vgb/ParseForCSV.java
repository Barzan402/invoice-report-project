package com.vgb;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

/*
 * This class is for reading the data from the CSV files
 * and creating objects for all 3 of Persons, Companies, and Items
 * in one class instead of 3 different classes
 */

public class ParseForCSV {
	
	/*
	 * Reads from the Persons.csv file and creates a list
	 * a list of person objects
	 */

    public static List<Person> readPersons(String filename) { // filename is the csv file with person data
		List<Person> persons = new ArrayList<>();
		File file = new File(filename);
		try {
			Scanner scanner = new Scanner(file);
			scanner.nextLine();
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] tokens = line.split(","); //Splits the line by commas into array

				if (tokens.length >= 4) { // Makes sure there are enough data fields to create a person
					UUID personUuid = UUID.fromString(tokens[0].trim()); //Converts the first column to a uuid
					String firstName = tokens[1].trim(); // Gets the first name
					String lastName = tokens[2].trim(); // Gets the last name
					String phone = tokens[3].trim(); // Gets the phone number

					List<String> emails = new ArrayList<>(); // Creates a list to store email addresses
					for (int i = 4; i < tokens.length; i++) { // Loops through additonal fields for emails
						emails.add(tokens[i].trim()); // This will add muiltiple emails if that person has more than one
					}
					// Creates a new person object and adds it to the list
					persons.add(new Person(personUuid, firstName, lastName, phone, emails));
				}
			}
			scanner.close();
		} catch (Exception e) {
		}
		return persons; // returns the list of persons
    }
    /*
     * Reads the companies.csv file and creates a list of company objects
     */

    // the filename is the csv file containing company data
    //the personList is a list of persons to find the companys contact person
    public static List<Company> readCompanies(String filename, List<Person> personsList) {
		List<Company> companies = new ArrayList<>(); // Creates an empty list for companies
		File file = new File(filename);
		try {
			Scanner scanner = new Scanner(file);
			scanner.nextLine();
			while (scanner.hasNextLine()) { // Reads the file line by line
				String line = scanner.nextLine();
				String[] tokens = line.split(","); 

				if (tokens.length >= 4) {
					UUID companyUuid = UUID.fromString(tokens[0].trim()); // Gets the company uuid
					UUID contactUuid = UUID.fromString(tokens[1].trim()); // Gets the contact persons uuid
					String name = tokens[2].trim(); // Gets the company name
					String street = tokens[3].trim(); // Gets the street name
					String city = tokens[4].trim(); // Gets the city name
					String state = tokens[5].trim(); // Gets the state name
					String zip = tokens[6].trim(); // gets the zip code
					Address address = new Address(street, city, state, zip); // Creates an address object
					Person contactPerson = null;
					//Finds the contact person based on their UUID
					for (Person person : personsList) {
						if (person.getPersonUuid().equals(contactUuid)) {
							contactPerson = person;
							break; // Stops searching after finding a match
						}
					}
					// Creates a new company object and adds it to the list
					companies.add(new Company(companyUuid, contactPerson, name, address));
				}
			}
			scanner.close();
		} catch (Exception e) {
		}
		return companies; // returns the list of companies
    } 
    
    /*
     * Reads the Items.csv file and creates a list of item objects
     */
    
    public static List<Item> readItems(String filename, List<Company> companiesList) {
    	List<Item> items = new ArrayList<>(); // Creates an empty list for items
    	File file = new File(filename);
    	try {
			Scanner scanner = new Scanner(file);
			scanner.nextLine();
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] tokens = line.split(",");

				if (tokens.length >= 3) { 
					UUID itemUuid = UUID.fromString(tokens[0].trim()); // Gets the item uuid
					char type = tokens[1].trim().charAt(0); // Gets the item type which is either (E,M,C)
					String name = tokens[2].trim(); // Gets the name of the item
					
					//Creates different item objects based on its type
					if (type == 'E' && tokens.length >= 5) { // Equipment
						String modelNumber = tokens[3].trim(); // Gets the model number
						double retailCost = Double.parseDouble(tokens[4].trim()); // Gets the retail pricce
						items.add(new Equipment(itemUuid, name, modelNumber, retailCost));

					} else if (type == 'M' && tokens.length >= 5) { // Material
						String unit = tokens[3].trim(); // Gets the unit type
						double pricePerUnit = Double.parseDouble(tokens[4].trim()); // gets the price per unit
						items.add(new Material(itemUuid, name, unit, pricePerUnit));

					} else if (type == 'C' && tokens.length >= 5) { // Contract
						UUID companyUuid = UUID.fromString(tokens[3].trim()); //Gets the contract company uuid
						double amount = Double.parseDouble(tokens[4].trim());
						Company servicer = null;
						// Finds the correct company for the contract
						for (Company company : companiesList) {
							if (company.getUuid().equals(companyUuid)) {
								servicer = company;
								break; // Stops searching when it finds a match
							}
						}
						// Adds a contract only if the correct company is found
						if (servicer != null) {
							items.add(new Contract(itemUuid, name, servicer, amount));
						}
					}
					}
			}
			scanner.close();
    	} catch (Exception e) {
    		
    	}

        return items; // returns the list of items
    }
    
    /*
     * Reads from the Invoices.csv file and creates a list of invoice objects
     */
    public static List<Invoice> readInvoices(String filename, List<Company> companiesList, List<Person> personsList) {
		List<Invoice> invoices = new ArrayList<>();
		File file = new File(filename);
		try {
			Scanner scanner = new Scanner(file);
			scanner.nextLine();
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] tokens = line.split(",");
				
				if (tokens.length >= 4) { // Make sure there are at least 4 pieces of data
					UUID invoiceUuid = UUID.fromString(tokens[0].trim()); // Gets invoice ID
					UUID customerUuid = UUID.fromString(tokens[1].trim()); // Gets customer ID
					UUID salespersonUuid = UUID.fromString(tokens[2].trim()); // Gets Salesperson ID
					LocalDate date = LocalDate.parse(tokens[3].trim()); // Gets Invoice Date
					
					Company customer = null;
					Person salesperson = null;
					// Finds matching company from the list
					for (Company company : companiesList) {
						if (company.getUuid().equals(customerUuid)) {
							customer = company;
							break;
						}
					}
					// Finds matching salesperson from the list
					for (Person person : personsList) {
						if (person.getPersonUuid().equals(salespersonUuid)) {
							salesperson = person;
							break;
						}
					}
					// If both company and salesperson found then it creates an invoice
					if (customer != null && salesperson != null) {
						invoices.add(new Invoice(invoiceUuid, customer, salesperson, date));
					}
				}
			}
			scanner.close();
		} catch (Exception e) {
		}
		return invoices;
    }
    
    /*
     * Reads from the InvoiceItems.csv file and adds items to the corresponding invoices
     */
    public static void readInvoiceItems(String filename, List<Invoice> invoicesList, List<Item> itemsList) {
		File file = new File(filename);
		try {
			Scanner scanner = new Scanner(file);
			scanner.nextLine();
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] tokens = line.split(",");

				if (tokens.length >= 2) {
					UUID invoiceUuid = UUID.fromString(tokens[0].trim()); // Gets invoice ID
					UUID itemUuid = UUID.fromString(tokens[1].trim()); // Gets Item ID
					
					Invoice invoice = null;
					Item item = null;
					// Find the matching Invoice
					for (Invoice inv : invoicesList) {
						if (inv.getInvoiceId().equals(invoiceUuid)) {
							invoice = inv;
							break;
						}
					}
					// Find the matching Item
					for (Item itm : itemsList) {
						if (itm.getUuid().equals(itemUuid)) {
							item = itm;
							break;
						}
					}
					// if both Invoice and item are found then it proccesses the item type
					if (invoice != null && item != null) {
						// Handles Equipment items
						if (item instanceof Equipment) {
							if (tokens.length == 3 && tokens[2].trim().equals("P")) {
								// Equipment purchase
								invoice.addItem(new Equipment(item.getUuid(), item.getName(), 
								((Equipment) item).getModelNumber(), ((Equipment) item).getRetailCost()));
                                
							} else if (tokens.length == 5 && tokens[2].trim().equals("L")) {
								//Equipment Lease
								LocalDate startDate = LocalDate.parse(tokens[3].trim());
								LocalDate endDate = LocalDate.parse(tokens[4].trim());
								invoice.addItem(new LeasedEquipment(item.getUuid(), item.getName(),
								((Equipment) item).getModelNumber(), ((Equipment) item).getRetailCost(),startDate, endDate));
                                
							} else if (tokens.length == 4 && tokens[2].trim().equals("R")) {
								// Equipment Rental
								int rentalHours = Integer.parseInt(tokens[3].trim());
								invoice.addItem(new RentedEquipment(item.getUuid(), item.getName(), 
								((Equipment) item).getModelNumber(), ((Equipment) item).getRetailCost(),rentalHours));
							}
						// Handles Material items
						} else if (item instanceof Material && tokens.length == 3) {
							int quantity = Integer.parseInt(tokens[2].trim());
							invoice.addItem(new Material(item.getUuid(), item.getName(),
							((Material) item).getUnit(), ((Material) item).getPricePerUnit(), quantity));
						// Handles Contract items
						} else if (item instanceof Contract && tokens.length == 3) {
							double contractAmount = Double.parseDouble(tokens[2].trim());
							invoice.addItem(new Contract(item.getUuid(), item.getName(),
							((Contract) item).getServicer(), contractAmount));
						}
					}
				}
			}
			scanner.close();
		} catch (Exception e) {
		}
    }

}
