package com.vgb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;
import java.sql.Date;

/**
 * This is a collection of utility methods that define a general API for
 * interacting with the database supporting this application.
 *
 */
public class InvoiceData {

	/**
	 * Removes all records from all tables in the database.
	 */
	public static void clearDatabase() {
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = DatabaseConnector.getConnection();
			
			ps = conn.prepareStatement("DELETE FROM InvoiceItem");
			ps.executeUpdate();
			ps.close();
			
			ps = conn.prepareStatement("DELETE FROM Invoice");
			ps.executeUpdate();
			ps.close();
			
			ps = conn.prepareStatement("DELETE FROM Item");
			ps.executeUpdate();
			ps.close();
			
			ps = conn.prepareStatement("DELETE FROM Company");
			ps.executeUpdate();
			ps.close();
			
			ps = conn.prepareStatement("DELETE FROM Address");
			ps.executeUpdate();
			ps.close();
			
			ps = conn.prepareStatement("DELETE FROM Email");
			ps.executeUpdate();
			ps.close();
			
			ps = conn.prepareStatement("DELETE FROM Person");
			ps.executeUpdate();
			ps.close();
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (ps != null) ps.close();
				if (conn != null) conn.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	/**
	 * Method to add a person record to the database with the provided data.
	 *
	 * @param personUuid
	 * @param firstName
	 * @param lastName
	 * @param phone
	 */
	public static void addPerson(UUID personUuid, String firstName, String lastName, String phone) {
		String sql = "INSERT INTO Person (uuid, firstName, lastName, phone) VALUES (?, ?, ?, ?)";
		
		try (Connection conn = DatabaseConnector.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)) {
			
			ps.setString(1, personUuid.toString());
			ps.setString(2, firstName);
			ps.setString(3, lastName);
			ps.setString(4, phone);
			
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static int getPersonId(UUID personUuid) {
		String sql = "SELECT personId FROM Person WHERE uuid = ?";
		
		try (Connection conn = DatabaseConnector.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)) {
			
			ps.setString(1, personUuid.toString());
			
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("personId");
				} else {
					throw new RuntimeException();
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Adds an email record corresponding person record corresponding to the
	 * provided <code>personUuid</code>
	 *
	 * @param personUuid
	 * @param email
	 */
	public static void addEmail(UUID personUuid, String email) {
		int personId = getPersonId(personUuid);
		
		String sql = "INSERT INTO Email (address, personId) VALUES (?, ?)";
		
		try (Connection conn = DatabaseConnector.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)) {
			
			ps.setString(1, email);
			ps.setInt(2, personId);
			
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static int getAddressId(String street, String city, String state, String zip) {
		String sql = "SELECT addressId FROM Address WHERE street = ? AND city = ? AND state = ? AND zip = ?";
		
		try (Connection conn = DatabaseConnector.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)) {
			
			ps.setString(1, street);
			ps.setString(2, city);
			ps.setString(3, state);
			ps.setString(4, zip);
			
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("addressId");
				} else {
					throw new RuntimeException();
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a company record to the database with the primary contact person identified by the
	 * given code.
	 *
	 * @param companyUuid
	 * @param name
	 * @param contactUuid
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 */
	public static void addCompany(UUID companyUuid, UUID contactUuid, String name, String street, String city, String state, String zip) {
		String insertAddressSql = "INSERT INTO Address (street, city, state, zip) VALUES (?, ?, ?, ?)";
		try (Connection conn = DatabaseConnector.getConnection();
			PreparedStatement ps = conn.prepareStatement(insertAddressSql)) {
			
			ps.setString(1, street);
			ps.setString(2, city);
			ps.setString(3, state);
			ps.setString(4, zip);
			
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		int addressId = getAddressId(street, city, state, zip);
		
		int contactId = getPersonId(contactUuid);
		
		String insertCompanySql = "INSERT INTO Company (uuid, name, contactId, addressId) VALUES (?, ?, ?, ?)";
		try (Connection conn = DatabaseConnector.getConnection();
			PreparedStatement ps = conn.prepareStatement(insertCompanySql)) {
			
			ps.setString(1, companyUuid.toString());
			ps.setString(2, name);
			ps.setInt(3, contactId);
			ps.setInt(4, addressId);
			
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	/**
	 * Adds an equipment record to the database of the given values.
	 *
	 * @param equipmentUuid
	 * @param name
	 * @param modelNumber
	 * @param retailPrice
	 */
	public static void addEquipment(UUID equipmentUuid, String name, String modelNumber, double retailPrice) {
		String sql = "INSERT INTO Item (uuid, name, type, modelNumber, retailPrice) VALUES (?, ?, 'E', ?, ?)";
		
		try (Connection conn = DatabaseConnector.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)) {
			
			ps.setString(1, equipmentUuid.toString());
			ps.setString(2, name);
			ps.setString(3, modelNumber);
			ps.setDouble(4, retailPrice);
			
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Adds an material record to the database of the given values.
	 *
	 * @param materialUuid
	 * @param name
	 * @param unit
	 * @param pricePerUnit
	 */
	public static void addMaterial(UUID materialUuid, String name, String unit, double pricePerUnit) {
		String sql = "INSERT INTO Item (uuid, name, type, unit, unitPrice) VALUES (?, ?, 'M', ?, ?)";
		
		try (Connection conn = DatabaseConnector.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)) {
			
			ps.setString(1, materialUuid.toString());
			ps.setString(2, name);
			ps.setString(3, unit);
			ps.setDouble(4, pricePerUnit);
			
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static int getCompanyId(UUID companyUuid) {
		String sql = "SELECT companyId FROM Company WHERE uuid = ?";
		
		try (Connection conn = DatabaseConnector.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)) {
			
			ps.setString(1, companyUuid.toString());
			
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("companyId");
				} else {
					throw new RuntimeException();
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds an contract record to the database of the given values.
	 *
	 * @param contractUuid
	 * @param name
	 * @param unit
	 * @param pricePerUnit
	 */
	public static void addContract(UUID contractUuid, String name, UUID servicerUuid) {
		int servicerCompanyId = getCompanyId(servicerUuid);
		
		String sql = "INSERT INTO Item (uuid, name, type, servicerCompanyId) VALUES (?, ?, 'C', ?)";
		
		try (Connection conn = DatabaseConnector.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)) {
			
			ps.setString(1, contractUuid.toString());
			ps.setString(2, name);
			ps.setInt(3, servicerCompanyId);
			
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Adds an Invoice record to the database with the given data.
	 *
	 * @param invoiceUuid
	 * @param customerUuid
	 * @param salesPersonUuid
	 * @param date
	 */
	public static void addInvoice(UUID invoiceUuid, UUID customerUuid, UUID salesPersonUuid, LocalDate date) {
		int customerId = getCompanyId(customerUuid);
		int salesPersonId = getPersonId(salesPersonUuid);
		
		String sql = "INSERT INTO Invoice (uuid, customerId, salesPersonId, invoiceDate) VALUES (?, ?, ?, ?)";
		
		try (Connection conn = DatabaseConnector.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)) {
			
			ps.setString(1, invoiceUuid.toString());
			ps.setInt(2, customerId);
			ps.setInt(3, salesPersonId);
			ps.setString(4, date.toString());
			
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static int getInvoiceId(UUID invoiceUuid) {
		String sql = "SELECT invoiceId FROM Invoice WHERE uuid = ?";
		
		try (Connection conn = DatabaseConnector.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)) {
			
			ps.setString(1, invoiceUuid.toString());
			
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("invoiceId");
				} else {
					throw new RuntimeException();
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static int getItemId(UUID itemUuid) {
		String sql = "SELECT itemId FROM Item WHERE uuid = ?";
	    
		try (Connection conn = DatabaseConnector.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)) {
			
			ps.setString(1, itemUuid.toString());
			
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("itemId");
				} else {
					throw new RuntimeException();
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Adds an Equipment purchase record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 */
	public static void addEquipmentPurchaseToInvoice(UUID invoiceUuid, UUID itemUuid) {
		int invoiceId = getInvoiceId(invoiceUuid);
		int itemId = getItemId(itemUuid);
		
		String sql = "INSERT INTO InvoiceItem (invoiceId, itemId, type) VALUES (?, ?, 'P')";
		
		try (Connection conn = DatabaseConnector.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)) {
			
			ps.setInt(1, invoiceId);
			ps.setInt(2, itemId);
			
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Adds an Equipment lease record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 * @param start
	 * @param end
	 */
	public static void addEquipmentLeaseToInvoice(UUID invoiceUuid, UUID itemUuid, LocalDate start, LocalDate end) {
		int invoiceId = getInvoiceId(invoiceUuid);
		int itemId = getItemId(itemUuid);
		
		String sql = "INSERT INTO InvoiceItem (invoiceId, itemId, type, leaseStart, leaseEnd) VALUES (?, ?, 'L', ?, ?)";
		
		try (Connection conn = DatabaseConnector.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)) {
			
			ps.setInt(1, invoiceId);
			ps.setInt(2, itemId);
			ps.setDate(3, Date.valueOf(start));
			ps.setDate(4, Date.valueOf(end));
			
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Adds an Equipment rental record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 * @param numberOfHours
	 */
	public static void addEquipmentRentalToInvoice(UUID invoiceUuid, UUID itemUuid, double numberOfHours) {
		int invoiceId = getInvoiceId(invoiceUuid);
		int itemId = getItemId(itemUuid);
		
		String sql = "INSERT INTO InvoiceItem (invoiceId, itemId, type, rentalHours) VALUES (?, ?, 'R', ?)";
		
		try (Connection conn = DatabaseConnector.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)) {
			
			ps.setInt(1, invoiceId);
			ps.setInt(2, itemId);
			ps.setDouble(3, numberOfHours);
			
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Adds a material record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 * @param numberOfUnits
	 */
	public static void addMaterialToInvoice(UUID invoiceUuid, UUID itemUuid, int numberOfUnits) {
		int invoiceId = getInvoiceId(invoiceUuid);
		int itemId = getItemId(itemUuid);
		
		String sql = "INSERT INTO InvoiceItem (invoiceId, itemId, type, quantity) VALUES (?, ?, 'M', ?)";
		
		try (Connection conn = DatabaseConnector.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)) {
			
			ps.setInt(1, invoiceId);
			ps.setInt(2, itemId);
			ps.setInt(3, numberOfUnits);
			
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Adds a contract record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 * @param amount
	 */
	public static void addContractToInvoice(UUID invoiceUuid, UUID itemUuid, double amount) {
		int invoiceId = getInvoiceId(invoiceUuid);
		int itemId = getItemId(itemUuid);
		
		String sql = "INSERT INTO InvoiceItem (invoiceId, itemId, type, contractAmount) VALUES (?, ?, 'C', ?)";
		
		try (Connection conn = DatabaseConnector.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)) {
			
			ps.setInt(1, invoiceId);
			ps.setInt(2, itemId);
			ps.setDouble(3, amount);
			
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
}