package com.cinco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This is a collection of utility methods that define a general API for
 * interacting with the database supporting this application.
 *
 */
public class InvoiceData {

	/**
	 * Method that removes every person record from the database
	 */
	public static void removeAllPersons() {

		//calls the method to remove all customers first
		//because persons are parents to customers in the database
		InvoiceData.removeAllCustomers();
		//calls the method to remove all products first
		//because persons are parents to products in the database
		InvoiceData.removeAllProducts();
		Database.classForName();
		Connection conn = Database.openConnection();
		String query = "delete from Email;";
		String query2 = "delete from Person";


		PreparedStatement ps = null; PreparedStatement p = null;
		ResultSet rs = null; ResultSet r = null;

		try {
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
			ps.close();

			p = conn.prepareStatement(query2);
			p.executeUpdate();
			p.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		Database.closeConnections(ps, rs, conn);
		Database.closeConnections(p, r, conn);

	}

	/**
	 * Removes the person record from the database corresponding to the
	 * provided <code>personCode</code>
	 * @param personCode
	 */
	public static void removePerson(String personCode) {

		Database.classForName();
		Connection conn = Database.openConnection();

		//Path 1, Deletion of email record:
		//deletion of email record:
		String query = "delete Email from "
					 + "Email inner join Person "
					 + "on Person.personId = Email.personId "
					 + "where Person.personCode = ?";

		//Path 2, Deletion of Consultant from Products:
		//removal of consultant rather than entire product record
		String query2 = "update Product "
					  + "inner join Person on "
					  + "Product.consultantPersonId = Person.personId "
					  + "set consultantPersonId = null "
					  + "where Person.personCode = ?;";

		//Path 3, Deletion of Salesperson from Invoice:
		//removal of salesperson rather than entire invoice record
		String query3 = "update Invoice "
				  + "inner join Person on "
				  + "Invoice.salespersonId = Person.personId "
				  + "set salespersonId = null "
				  + "where Person.personCode = ?;";

		//Path 4, Deletion of primary contact from Customer
		//removal of primary contact rather than entire customer record
		String query4 = "update Customer "
				  + "inner join Person on "
				  + "Customer.contactPersonId = Person.personId "
				  + "set contactPersonId = null "
				  + "where Person.personCode = ?;";

		//Finally, deletion of the Person record:
		String query5 = "delete from Person where Person.personCode = ?;";

		PreparedStatement ps = null; PreparedStatement p2 = null;
		PreparedStatement p3 = null; PreparedStatement p4 = null;
		PreparedStatement p5 = null;

		ResultSet rs = null; ResultSet r2 = null;
		ResultSet r3 = null; ResultSet r4 = null;
		ResultSet r5 = null;

		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, personCode);
			ps.executeUpdate();
			ps.close();
			p2 = conn.prepareStatement(query2);
			p2.setString(1, personCode);
			p2.executeUpdate();
			p2.close();
			p3 = conn.prepareStatement(query3);
			p3.setString(1, personCode);
			p3.executeUpdate();
			p3.close();
			p4 = conn.prepareStatement(query4);
			p4.setString(1, personCode);
			p4.executeUpdate();
			p4.close();
			p5 = conn.prepareStatement(query5);
			p5.setString(1, personCode);
			p5.executeUpdate();
			p5.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		Database.closeConnections(ps, rs, conn);
		Database.closeConnections(p2, r2, conn);
		Database.closeConnections(p3, r3, conn);
		Database.closeConnections(p4, r4, conn);
		Database.closeConnections(p5, r5, conn);
	}

	/**
	 * Method to add a person record to the database with the provided data.
	 * @param personCode
	 * @param firstName
	 * @param lastName
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 * @param country
	 */
	public static void addPerson(String personCode, String firstName, String lastName,
			String street, String city, String state, String zip, String country) {

		Database.classForName();
		Connection conn = Database.openConnection();

		//creates a new address record with given information:
		String query = "insert into Address (street, city, state, zip, country) values (?, ?, ?, ?, ?);";

		//creates a new person record:
		String query2 = "insert into Person (personCode, lastName, firstName, addressId) values (?, ?, ?, ?);";

		PreparedStatement ps = null; PreparedStatement p2 = null;
		ResultSet rs = null; ResultSet r2 = null;

		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, street);
			ps.setString(2, city);
			ps.setString(3, state);
			ps.setString(4, zip);
			ps.setString(5, country);
			ps.executeUpdate();

			//Need this query in order obtain the addressId just created above:
			PreparedStatement prep;
			prep = conn.prepareStatement("SELECT LAST_INSERT_ID()");
			ResultSet res = prep.executeQuery();
			res.next();
			int foreignKeyId = res.getInt("LAST_INSERT_ID()");

			p2 = conn.prepareStatement(query2);
			p2.setString(1, personCode);
			p2.setString(2, lastName);
			p2.setString(3, firstName);
			p2.setInt(4, foreignKeyId);
			p2.executeUpdate();

		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		Database.closeConnections(ps, rs, conn);
		Database.closeConnections(p2, r2, conn);

	}

	/**
	 * Adds an email record corresponding person record corresponding to the
	 * provided <code>personCode</code>
	 * @param personCode
	 * @param email
	 */
	public static void addEmail(String personCode, String email) {

		Database.classForName();
		Connection conn = Database.openConnection();

		//need this query in order to get a personId from the personCode given:
		String query = "select personId from Person where personCode = ?;";

		String query2 = "insert into Email (personId, email) values (?, ?);";

		PreparedStatement ps = null; PreparedStatement p2 = null;
		ResultSet rs = null; ResultSet r2 = null;
		int personId;

		try {

			ps = conn.prepareStatement(query);
			ps.setString(1, personCode);
			rs = ps.executeQuery();
			if(rs.next()) {
				personId = rs.getInt("personId");
			} else {
				throw new IllegalStateException("No person in database with code = " + personCode);
			}
			rs.close();
			ps.close();

			p2 = conn.prepareStatement(query2);
			p2.setInt(1, personId);
			p2.setString(2, email);
			p2.executeUpdate();


		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		Database.closeConnections(ps, rs, conn);
		Database.closeConnections(p2, r2, conn);

	}

	/**
	 * Method that removes every customer record from the database
	 */
	public static void removeAllCustomers() {

		//calls method to remove all invoices first
		//because customer are parents to invoices in the database
		InvoiceData.removeAllInvoices();
		Database.classForName();
		Connection conn = Database.openConnection();
		String query = "delete from Customer;";


		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		Database.closeConnections(ps, rs, conn);
	}

	public static void addCustomer(String customerCode, String type, String primaryContactPersonCode, String name,
			String street, String city, String state, String zip, String country) {

		Database.classForName();
		Connection conn = Database.openConnection();

		//creating a new address record:
		String query = "insert into Address (street, city, state, zip, country) values (?, ?, ?, ?, ?);";

		//getting the personId from the given primaryContactPersonCode
		String query2 = "select personId from Person where personCode = ?;";

		String query3 = "insert into Customer (customerCode, customerType, contactPersonId, customerName, addressId) values (?, ?, ?, ?, ?);";

		PreparedStatement ps = null; PreparedStatement p2 = null;
		PreparedStatement p3 = null;
		ResultSet rs = null; ResultSet r2 = null;
		ResultSet r3 = null;
		int primaryContactId;

		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, street);
			ps.setString(2, city);
			ps.setString(3, state);
			ps.setString(4, zip);
			ps.setString(5, country);
			ps.executeUpdate();

			//need this query in order to access the newly created addressId from above:
			PreparedStatement prep;
			prep = conn.prepareStatement("SELECT LAST_INSERT_ID()");
			ResultSet res = prep.executeQuery();
			res.next();
			int foreignKeyId = res.getInt("LAST_INSERT_ID()");

			p2 = conn.prepareStatement(query2);
			p2.setString(1, primaryContactPersonCode);
			r2 = p2.executeQuery();
			if(r2.next()) {
				primaryContactId = r2.getInt("personId");
			} else {
				throw new IllegalStateException("No person in database with code = " + primaryContactPersonCode);
			}
			r2.close();
			p2.close();

			p3 = conn.prepareStatement(query3);
			p3.setString(1, customerCode);
			p3.setString(2, type);
			p3.setInt(3, primaryContactId);
			p3.setString(4, name);
			p3.setInt(5, foreignKeyId);
			p3.executeUpdate();


		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		Database.closeConnections(ps, rs, conn);
		Database.closeConnections(p2, r2, conn);
		Database.closeConnections(p3, r3, conn);

	}

	/**
	 * Removes all product records from the database
	 */
	public static void removeAllProducts() {
		InvoiceData.removeAllInvoices();
		Database.classForName();
		Connection conn = Database.openConnection();
		String query = "delete from Product;";


		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		Database.closeConnections(ps, rs, conn);
	}

	/**
	 * Removes a particular product record from the database corresponding to the
	 * provided <code>productCode</code>
	 * @param assetCode
	 */
	public static void removeProduct(String productCode) {

		Database.classForName();
		Connection conn = Database.openConnection();

		//The decision was made to also delete the product from the invoice
		//so as to say it was out of stock/discontinued/etc.
		String query = "delete Purchase "
					 + "from Purchase inner join "
					 + "Product on "
					 + "Purchase.productId = Product.productId "
					 + "where productCode = ?;";

		String query2 = "delete from Product where productCode = ?";

		PreparedStatement ps = null; PreparedStatement p = null;
		ResultSet rs = null; ResultSet r = null;

		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, productCode);
			ps.executeUpdate();
			ps.close();
			p = conn.prepareStatement(query2);
			p.setString(1, productCode);
			p.executeUpdate();
			p.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		Database.closeConnections(ps, rs, conn);
		Database.closeConnections(p, r, conn);

	}

	/**
	 * Adds an equipment record to the database with the
	 * provided data.
	 */
	public static void addEquipment(String productCode, String name, Double pricePerUnit) {

		Database.classForName();
		Connection conn = Database.openConnection();

		String query = "insert into Product (productCode, productType, productName, pricePerUnit) values (?, 'E', ?, ?);";

		PreparedStatement p = null;
		ResultSet r = null;

		try {
			p = conn.prepareStatement(query);
			p.setString(1, productCode);
			p.setString(2, name);
			p.setDouble(3, pricePerUnit);
			p.executeUpdate();

		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		Database.closeConnections(p, r, conn);


	}

	/**
	 * Adds an license record to the database with the
	 * provided data.
	 */
	public static void addLicense(String productCode, String name, double serviceFee, double annualFee) {

		Database.classForName();
		Connection conn = Database.openConnection();

		String query = "insert into Product (productCode, productType, productName, serviceFee, annualLicenseFee) values (?, 'L', ?, ?, ?);";

		PreparedStatement p = null;
		ResultSet r = null;

		try {
			p = conn.prepareStatement(query);
			p.setString(1, productCode);
			p.setString(2, name);
			p.setDouble(3, serviceFee);
			p.setDouble(4, annualFee);
			p.executeUpdate();

		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		Database.closeConnections(p, r, conn);

	}

	/**
	 * Adds an consultation record to the database with the
	 * provided data.
	 */
	public static void addConsultation(String productCode, String name, String consultantPersonCode, Double hourlyFee) {

		Database.classForName();
		Connection conn = Database.openConnection();

		//getting a personId from the given consultantPersonCode
		String query = "select personId from Person where personCode = ?;";

		String query2 = "insert into Product (productCode, productType, productName, consultantPersonId, hourlyFee) values (?, 'C', ?, ?, ?);";



		PreparedStatement ps = null; PreparedStatement p = null;
		ResultSet rs = null; ResultSet r = null;
		int consultantId;

		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, consultantPersonCode);
			rs = ps.executeQuery();
			if(rs.next()) {
				consultantId = rs.getInt("personId");
			} else {
				throw new IllegalStateException("No consultant in database with code = " + consultantPersonCode);
			}
			rs.close();
			ps.close();

			p = conn.prepareStatement(query2);
			p.setString(1, productCode);
			p.setString(2, name);
			p.setInt(3, consultantId);
			p.setDouble(4, hourlyFee);
			p.executeUpdate();

		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		Database.closeConnections(ps, rs, conn);
		Database.closeConnections(p, r, conn);

	}

	/**
	 * Removes all invoice records from the database
	 */
	public static void removeAllInvoices() {
		Database.classForName();
		Connection conn = Database.openConnection();
		String query = "delete from Purchase;";
		String query2 = "delete from Invoice;";

		PreparedStatement ps = null; PreparedStatement p = null;
		ResultSet rs = null; ResultSet r = null;

		try {
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
			ps.close();
			p = conn.prepareStatement(query2);
			p.executeUpdate();
			p.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		Database.closeConnections(ps, rs, conn);
		Database.closeConnections(p, r, conn);

	}

	/**
	 * Removes the invoice record from the database corresponding to the
	 * provided <code>invoiceCode</code>
	 * @param invoiceCode
	 */
	public static void removeInvoice(String invoiceCode) {

		Database.classForName();
		Connection conn = Database.openConnection();

		//first, the deletion of all purchases corresponding to an invoice
		String query = "delete Purchase "
					 + "from Purchase inner join "
					 + "Invoice on "
					 + "Purchase.invoiceId = Invoice.invoiceId "
					 + "where invoiceCode = ?;";

		String query2 = "delete from Invoice where invoiceCode = ?";

		PreparedStatement ps = null; PreparedStatement p = null;
		ResultSet rs = null; ResultSet r = null;

		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, invoiceCode);
			ps.executeUpdate();
			ps.close();
			p = conn.prepareStatement(query2);
			p.setString(1, invoiceCode);
			p.executeUpdate();
			p.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		Database.closeConnections(ps, rs, conn);
		Database.closeConnections(p, r, conn);


	}

	/**
	 * Adds an invoice record to the database with the given data.
	 */
	public static void addInvoice(String invoiceCode, String customerCode, String salesPersonCode) {

		Database.classForName();
		Connection conn = Database.openConnection();

		//getting a customerId from the given customerCode:
		String query = "select customerId from Customer where customerCode = ?;";

		//getting a personId from the given salespersonCode:
		String query2 = "select personId from Person where personCode = ?;";

		String query3 = "insert into Invoice (invoiceCode, customerId, salespersonId) values (?, ?, ?);";

		PreparedStatement ps = null; PreparedStatement p2 = null;
		PreparedStatement p3 = null;
		ResultSet rs = null; ResultSet r2 = null;
		ResultSet r3 = null;
		int customerId;
		int salespersonId;

		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, customerCode);
			rs = ps.executeQuery();
			if(rs.next()) {
				customerId = rs.getInt("customerId");
			} else {
				throw new IllegalStateException("No customer in database with code = " + customerCode);
			}
			rs.close();
			ps.close();

			p2 = conn.prepareStatement(query2);
			p2.setString(1, salesPersonCode);
			r2 = p2.executeQuery();
			if(r2.next()) {
				salespersonId = r2.getInt("personId");
			} else {
				throw new IllegalStateException("No salesperson in database with code = " + salesPersonCode);
			}
			r2.close();
			p2.close();

			p3 = conn.prepareStatement(query3);
			p3.setString(1, invoiceCode);
			p3.setInt(2, customerId);
			p3.setInt(3, salespersonId);
			p3.executeUpdate();


		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		Database.closeConnections(ps, rs, conn);
		Database.closeConnections(p2, r2, conn);
		Database.closeConnections(p3, r3, conn);

	}

	/**
	 * Adds a particular equipment (corresponding to <code>productCode</code> to an
	 * invoice corresponding to the provided <code>invoiceCode</code> with the given
	 * number of units
	 */
	public static void addEquipmentToInvoice(String invoiceCode, String productCode, int numUnits) {

		Database.classForName();
		Connection conn = Database.openConnection();

		//getting a productId from the given productCode:
		String query = "select productId from Product where productCode = ?;";

		//getting an invoiceId form the given invoiceCode:
		String query2 = "select invoiceId from Invoice where invoiceCode = ?;";

		String query3 = "insert into Purchase (invoiceId, productId, units) values (?, ?, ?);";

		PreparedStatement ps = null; PreparedStatement p2 = null;
		PreparedStatement p3 = null;
		ResultSet rs = null; ResultSet r2 = null;
		ResultSet r3 = null;
		int productId;
		int invoiceId;

		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, productCode);
			rs = ps.executeQuery();
			if(rs.next()) {
				productId = rs.getInt("productId");
			} else {
				throw new IllegalStateException("No product in database with code = " + productCode);
			}
			rs.close();
			ps.close();

			p2 = conn.prepareStatement(query2);
			p2.setString(1, invoiceCode);
			r2 = p2.executeQuery();
			if(r2.next()) {
				invoiceId = r2.getInt("invoiceId");
			} else {
				throw new IllegalStateException("No invoice in database with code = " + invoiceCode);
			}
			r2.close();
			p2.close();

			p3 = conn.prepareStatement(query3);
			p3.setInt(1, invoiceId);
			p3.setInt(2, productId);
			p3.setInt(3, numUnits);
			p3.executeUpdate();


		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		Database.closeConnections(ps, rs, conn);
		Database.closeConnections(p2, r2, conn);
		Database.closeConnections(p3, r3, conn);

	}

	/**
	 * Adds a particular equipment (corresponding to <code>productCode</code> to an
	 * invoice corresponding to the provided <code>invoiceCode</code> with the given
	 * begin/end dates
	 */
	public static void addLicenseToInvoice(String invoiceCode, String productCode, String startDate, String endDate) {

		Database.classForName();
		Connection conn = Database.openConnection();

		//getting a productId from the given productCode:
		String query = "select productId from Product where productCode = ?;";

		//getting an invoiceId from the given invoiceCode:
		String query2 = "select invoiceId from Invoice where invoiceCode = ?;";

		String query3 = "insert into Purchase (invoiceId, productId, dateStart, dateEnd) values (?, ?, ?, ?);";

		PreparedStatement ps = null; PreparedStatement p2 = null;
		PreparedStatement p3 = null;
		ResultSet rs = null; ResultSet r2 = null;
		ResultSet r3 = null;
		int productId;
		int invoiceId;

		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, productCode);
			rs = ps.executeQuery();
			if(rs.next()) {
				productId = rs.getInt("productId");
			} else {
				throw new IllegalStateException("No product in database with code = " + productCode);
			}
			rs.close();
			ps.close();

			p2 = conn.prepareStatement(query2);
			p2.setString(1, invoiceCode);
			r2 = p2.executeQuery();
			if(r2.next()) {
				invoiceId = r2.getInt("invoiceId");
			} else {
				throw new IllegalStateException("No invoice in database with code = " + invoiceCode);
			}
			r2.close();
			p2.close();

			p3 = conn.prepareStatement(query3);
			p3.setInt(1, invoiceId);
			p3.setInt(2, productId);
			p3.setString(3, startDate);
			p3.setString(4, endDate);
			p3.executeUpdate();


		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		Database.closeConnections(ps, rs, conn);
		Database.closeConnections(p2, r2, conn);
		Database.closeConnections(p3, r3, conn);

	}

	/**
	 * Adds a particular equipment (corresponding to <code>productCode</code> to an
	 * invoice corresponding to the provided <code>invoiceCode</code> with the given
	 * number of billable hours.
	 */
	public static void addConsultationToInvoice(String invoiceCode, String productCode, double numHours) {

		Database.classForName();
		Connection conn = Database.openConnection();

		//getting a productId from the given productCode:
		String query = "select productId from Product where productCode = ?;";

		//getting an invoiceId from the given invoiceCode:
		String query2 = "select invoiceId from Invoice where invoiceCode = ?;";

		String query3 = "insert into Purchase (invoiceId, productId, hours) values (?, ?, ?);";

		PreparedStatement ps = null; PreparedStatement p2 = null;
		PreparedStatement p3 = null;
		ResultSet rs = null; ResultSet r2 = null;
		ResultSet r3 = null;
		int productId;
		int invoiceId;

		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, productCode);
			rs = ps.executeQuery();
			if(rs.next()) {
				productId = rs.getInt("productId");
			} else {
				throw new IllegalStateException("No product in database with code = " + productCode);
			}
			rs.close();
			ps.close();

			p2 = conn.prepareStatement(query2);
			p2.setString(1, invoiceCode);
			r2 = p2.executeQuery();
			if(r2.next()) {
				invoiceId = r2.getInt("invoiceId");
			} else {
				throw new IllegalStateException("No invoice in database with code = " + invoiceCode);
			}
			r2.close();
			p2.close();

			p3 = conn.prepareStatement(query3);
			p3.setInt(1, invoiceId);
			p3.setInt(2, productId);
			p3.setDouble(3, numHours);
			p3.executeUpdate();


		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		Database.closeConnections(ps, rs, conn);
		Database.closeConnections(p2, r2, conn);
		Database.closeConnections(p3, r3, conn);


	}

}



