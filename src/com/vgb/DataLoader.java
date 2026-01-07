package com.vgb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
/**
 * This class is responsible for loading data from the SQL database
 * and converting it into Java objects.
 * 
 * It connects to the database using JDBC and SQL queries to retrieve information
 * about people, companies, addresses, items, invoices, and the invoice items.
 */
public class DataLoader {
	//This method loads all Person records from the database and returns them as a list
	public static List<Person> loadPersons() {
		List<Person> persons = new ArrayList<>(); // Creates an empty list to hold Person objects from the database
		// Declare variables for databse connections
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		// The SQL query to select the correct columns from the Person table
		String sql = "SELECT personId, uuid, firstName, lastName, phone FROM Person";
		
		try {
			conn = DatabaseConnector.getConnection(); //Gets the connection from the database connecter class
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			// Loops through each row in the result set
			while (rs.next()) {
				int personId = rs.getInt("personId");
				UUID uuid = UUID.fromString(rs.getString("uuid"));
				String firstName = rs.getString("firstName");
				String lastName = rs.getString("lastName");
				String phone = rs.getString("phone");
				
				// Creates a new Person Object and will get the emails from the email loader
				persons.add(new Person(personId, uuid, firstName, lastName, phone, new ArrayList<>()));
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		// Closes all the resources
		try {
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		// Returns the list of Person objects built 
		return persons;
	}

	// Loads email addresses from the Email table and adds them to the correct Person
	public static void loadEmails(List<Person> people) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		// SQL query to get email address and the personId it belongs to
		String sql = "SELECT address, personId FROM Email";
		
		try {
			conn = DatabaseConnector.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			// Loops through each row
			while (rs.next()) {
				String address = rs.getString("address");
				int personId = rs.getInt("personId");
				// Goes through the list of Person objects
				for (Person p : people) {
					// If it finds the matching person by Id,
					// it will then add the email address to that person's email list
					// then it will stop checking once found and added.
					if (p.getPersonId() == personId) {
						p.getEmails().add(address);
						break; 
					}
				}
			}
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		try {
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	// Loads Company records and connects them to their contact Person
	public static List<Company> loadCompanies(List<Person> people, List<Address> addresses) {
		// Creates an empty list to store Company objects
		List<Company> companies = new ArrayList<>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		// SQL query to get the correct columns from the Company table
	    String sql = "SELECT companyId, uuid, name, contactId, addressId FROM Company";
	    
	    try {
			conn = DatabaseConnector.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			// Loops through each result from the database
			while (rs.next()) {
	            int companyId = rs.getInt("companyId");
				UUID uuid = UUID.fromString(rs.getString("uuid"));
				String name = rs.getString("name");
				int contactId = rs.getInt("contactId");
				int addressId = rs.getInt("addressId");
				// Finds the matching Person using contactId
				Person contact = null;
				for (Person p : people) {
					if (p.getPersonId() == contactId) {
				    	contact = p;
				        break;
				    }
				}
				// Finds the matching Address using addressId
				Address address = null;
				for (Address a : addresses) {
					if (a.getAddressId() == addressId) {
						address = a;
						break;
					}
				}
				// Adds the new Company to the list
				companies.add(new Company(companyId, uuid, contact, name, address));
			}
	    } catch (SQLException e) {
	    	throw new RuntimeException(e);
	    }
	    
	    try {
	    	if (rs != null && !rs.isClosed()) {
	    		rs.close();
	    	}
	    	if (ps != null && !ps.isClosed()) {
	    		ps.close();
	    	}
	    	if (conn != null && !conn.isClosed()) {
	    		conn.close();
	    	}
	    } catch (SQLException e) {
	    	throw new RuntimeException(e);
		}
		// Returns the completed list of Company objects
		return companies;
	}
	
	// Loads addresses from the Address table and assigns them to the correct Company
	public static List<Address> loadAddresses() {
		// Creates a list to store all Address objects
		List<Address> addresses = new ArrayList<>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		// SQL query to select addresses from the Address table
		String sql = "SELECT addressId, street, city, state, zip FROM Address";
		
		try {
			conn = DatabaseConnector.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			// Loops through each row in the result set
			while (rs.next()) {
				int addressId = rs.getInt("addressId");
				String street = rs.getString("street");
				String city = rs.getString("city");
				String state = rs.getString("state");
				String zip = rs.getString("zip");
				// Creates a new Address object and adds it to the list
				Address address = new Address(addressId, street, city, state, zip);
				addresses.add(address);
			}
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		try {
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		// Returns the full list of Address objects
		return addresses;
	}
	
	// Loads all the items (Equipment "E", Material "M", Contract "C") from the database
	public static List<Item> loadItems(List<Company> companies) {
		// Creates a list to hold all item objects
		List<Item> items = new ArrayList<>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		// SQL query to get all item data from the Item table
		String sql = "SELECT uuid, type, name, modelNumber, retailPrice, unit, unitPrice, servicerCompanyId FROM Item";
		
		try {
			conn = DatabaseConnector.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			// Loops through each row of the item data
			while (rs.next()) {
				UUID uuid = UUID.fromString(rs.getString("uuid"));
				String type = rs.getString("type");
				String name = rs.getString("name");
				// If the item is Equipment
				if ("E".equals(type)) {
					String modelNumber = rs.getString("modelNumber");
					double retailPrice = rs.getDouble("retailPrice");
					items.add(new Equipment(uuid, name, modelNumber, retailPrice));
					// If the item is Material
				} else if ("M".equals(type)) {
					String unit = rs.getString("unit");
					double unitPrice = rs.getDouble("unitPrice");
					items.add(new Material(uuid, name, unit, unitPrice));
					// If the item is a Contract
				} else if ("C".equals(type)) {
					int servicerCompanyId = rs.getInt("servicerCompanyId");
					Company servicer = null;
					// Finds the correct Company to associate as servicer
					for (Company c : companies) {
						if (c.getCompanyId() == servicerCompanyId) {
							servicer = c;
							break;
						}
					}
					// Only adds the contract if a matching servicer is found
					if (servicer != null) {
					    items.add(new Contract(uuid, name, servicer, 0.0));
					}
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		try {
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}
			if (ps != null && !ps.isClosed()) {
				ps.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		// Returns the list of loaded items
		return items;
	}
	
	// Loads Invoice records from the database and links each invoice to a Company (customer)
	// and Person (salesperson) 
	public static List<Invoice> loadInvoices(List<Company> companies, List<Person> people) {
		// Creates a list to store all invoice objects
		List<Invoice> invoices = new ArrayList<>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		// SQL query to retrieve invoice details
		String sql = "SELECT uuid, customerId, salespersonId, invoiceDate FROM Invoice";
		
		try {
			conn = DatabaseConnector.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			// Loop through each row returned from the database
			while (rs.next()) {
				UUID uuid = UUID.fromString(rs.getString("uuid"));
				int customerId = rs.getInt("customerId");
				int salespersonId = rs.getInt("salespersonId");
				LocalDate date = rs.getDate("invoiceDate").toLocalDate();
				// Find the matching company (customer) using customerId
				Company customer = null;
				for (Company c : companies) {
					if (c.getCompanyId() == customerId) {
						customer = c;
						break;
					}
				}
				// Find the matching person (salesperson) using salespersonId
				Person salesperson = null;
				for (Person p : people) {
					if (p.getPersonId() == salespersonId) {
						salesperson = p;
						break;
					}
				}
				// Only create and add invoice if both customer and salesperson are found
				if (customer != null && salesperson != null) {
					Invoice invoice = new Invoice(uuid, customer, salesperson, date);
					invoices.add(invoice);
				}
			}
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
				if (ps != null && !ps.isClosed()) {
					ps.close();
				}
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		// Returns the complete list of invoices
		return invoices;
	}
	
	// Loads the invoice item data and creates the correct item types with their specific details
	public static void loadInvoiceItems(List<Invoice> invoices, List<Item> items) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		// SQL query to get invoice items and join them with invoice and item info
		String sql = "SELECT Invoice.uuid AS invoiceUuid, " +
					"Item.uuid AS itemUuid, " +
					"InvoiceItem.type, InvoiceItem.quantity, " +
					"InvoiceItem.rentalHours, InvoiceItem.leaseStart, InvoiceItem.leaseEnd, " +
					"InvoiceItem.contractAmount, " +
					"Item.retailPrice " +
					"FROM InvoiceItem " +
					"JOIN Invoice ON InvoiceItem.invoiceId = Invoice.invoiceId " +
					"JOIN Item ON InvoiceItem.itemId = Item.itemId";
		
		try {
			conn = DatabaseConnector.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			// Loops through each row in the result set
			while (rs.next()) {
				UUID invoiceUuid = UUID.fromString(rs.getString("invoiceUuid"));
				UUID itemUuid = UUID.fromString(rs.getString("itemUuid"));
				String type = rs.getString("type");
				// Finds the matching invoice by UUID
				Invoice targetInvoice = null;
				for (Invoice inv : invoices) {
					if (inv.getInvoiceId().equals(invoiceUuid)) {
						targetInvoice = inv;
						break;
					}
				}
				// Finds the matching base item by UUID
				Item baseItem = null;
				for (Item item : items) {
					if (item.getUuid().equals(itemUuid)) {
						baseItem = item;
						break;
					}
				}
				// If both invoice and item are found, create the specific item type
				if (targetInvoice != null && baseItem != null) {
					Item newItem = null;
					
					// Handle purchase item
					if ("P".equals(type)) {
						newItem = baseItem;
	                   
						// Handles rental item
					} else if ("R".equals(type) && baseItem instanceof Equipment) {
						String model = ((Equipment) baseItem).getModelNumber();
						double price = rs.getDouble("retailPrice");
						double hours = rs.getDouble("rentalHours");
						newItem = new RentedEquipment(baseItem.getUuid(), baseItem.getName(), model, price, hours);
						   
						// Handles lease item
					} else if ("L".equals(type) && baseItem instanceof Equipment) {
						String model = ((Equipment) baseItem).getModelNumber();
						double price = rs.getDouble("retailPrice");
						LocalDate start = rs.getDate("leaseStart").toLocalDate();
						LocalDate end = rs.getDate("leaseEnd").toLocalDate();
						newItem = new LeasedEquipment(baseItem.getUuid(), baseItem.getName(), model, price, start, end);
						
						// Handles contract item
					} else if ("C".equals(type) && baseItem instanceof Contract) {
						Contract original = (Contract) baseItem;
						Company servicer = original.getServicer();
						double amount = rs.getDouble("contractAmount");
						newItem = new Contract(baseItem.getUuid(), baseItem.getName(), servicer, amount);
						
						// Handles material item
					} else if ("M".equals(type) && baseItem instanceof Material) {
						String unit = ((Material) baseItem).getUnit();
						double pricePerUnit = ((Material) baseItem).getPricePerUnit();
						int quantity = rs.getInt("quantity");
						newItem = new Material(baseItem.getUuid(), baseItem.getName(), unit, pricePerUnit, quantity);
					}
					// Add the new item to the invoice
					if (newItem != null) {
						targetInvoice.addItem(newItem);
					}
				}
			}
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
				if (ps != null && !ps.isClosed()) {
					ps.close();
				}
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
}
