package com.cinco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

public class Customer implements Comparable<Customer> {

	//declaration of the contents of the Customer object
	private String customerCode;
	private String type;
	private Person primaryContact;
	private String customerName;
	private Address customerAddress;

	//instantiation of a logger
	public static Logger log = Logger.getLogger(Invoice.class);

	//constructor for the Customer object
	public Customer(String customerCode, String type, String customerName,
					Address customerAddress, Person primaryContact) {

		//"this" keyword to eliminate ambiguities
		this.customerCode = customerCode;
		this.type = type;
		this.primaryContact = primaryContact;
		this.customerName = customerName;
		this.customerAddress = customerAddress;
	}

	//allows the Customer object to be empty
	public Customer() {
	}

	//below are all the generated getters and setters
	public String getCustomerCode() {
		return customerCode;
	}

	public static Logger getLog() {
		return log;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Person getPrimaryContact() {
		return primaryContact;
	}

	public void setPrimaryContact(Person primaryContact) {
		this.primaryContact = primaryContact;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Address getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(Address customerAddress) {
		this.customerAddress = customerAddress;
	}

	//method that returns the customer that corresponds to the given customerId using JDBC
	public static Customer getCustomer(int customerId) {
		Customer c = new Customer();

		Database.classForName();

		Connection conn = Database.openConnection();

		String query = "select customerCode, "
					 + "customerType, "
					 + "customerName, "
					 + "addressId, "
					 + "contactPersonId "
					 + "from Customer where customerId = ?;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			//sets the ? equal to customerId:
			ps.setInt(1, customerId);
			rs = ps.executeQuery();
			//checks for entries after executing the query:
			if(rs.next()) {
				//extracting data from sql table and assigning variables:
				String customerCode = rs.getString("customerCode");
				String customerType = rs.getString("customerType");
				String customerName = rs.getString("customerName");
				int addressId = rs.getInt("addressId");
				int contactPersonId = rs.getInt("contactPersonId");
				Address a = Address.getAddress(addressId);
				Person p = Person.getPerson(contactPersonId);
				c = new Customer(customerCode, customerType, customerName, a, p);
			} else {
				throw new IllegalStateException("No customer in database with id = " + customerId);
			}
			rs.close();
			ps.close();

		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			log.error("Could not complete query", e);
			throw new RuntimeException(e);
		}

		Database.closeConnections(ps, rs, conn);
		return c;
	}

	//method that returns a list of customers using JDBC
	//similar procedure as the method above
	public static List<Customer> getCustomerList() {

		List<Customer> customer = new ArrayList<>();

		Database.classForName();

		Connection conn = Database.openConnection();

		String query = "select customerCode, "
					 + "customerType, "
					 + "customerName, "
					 + "addressId, "
					 + "contactPersonId "
					 + "from Customer;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			//iterates through each row of the table:
			while(rs.next()) {
				String customerCode = rs.getString("customerCode");
				String customerType = rs.getString("customerType");
				String customerName = rs.getString("customerName");
				int addressId = rs.getInt("addressId");
				int contactPersonId = rs.getInt("contactPersonId");
				Address a = Address.getAddress(addressId);
				Person p = Person.getPerson(contactPersonId);
				Customer c = new Customer(customerCode, customerType, customerName, a, p);
				customer.add(c);
			}

		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			log.error("Could not complete query", e);
			throw new RuntimeException(e);
		}

		Database.closeConnections(ps, rs, conn);

		return customer;
	}

	//method that returns a list of customers using JDBC
	//similar procedure as the method above
	//sorted by name using compareTo method below
	public static List<Customer> getSortedCustomerList() {

		List<Customer> customer = new ArrayList<>();

		Database.classForName();

		Connection conn = Database.openConnection();

		String query = "select customerCode, "
					 + "customerType, "
					 + "customerName, "
					 + "addressId, "
					 + "contactPersonId "
					 + "from Customer;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			//iterates through each row of the table:
			while(rs.next()) {
				String customerCode = rs.getString("customerCode");
				String customerType = rs.getString("customerType");
				String customerName = rs.getString("customerName");
				int addressId = rs.getInt("addressId");
				int contactPersonId = rs.getInt("contactPersonId");
				Address a = Address.getAddress(addressId);
				Person p = Person.getPerson(contactPersonId);
				Customer c = new Customer(customerCode, customerType, customerName, a, p);

				//binary search takes a list that extend comparable and a key for input
				//it then uses those to search for the correct insertion index to produce
				//a sorted list
				int index = Collections.binarySearch(customer, c);
				//Customer objects extend comparable, so the search can begin:
				//when binary search does not match an index exactly, it returns a
				//negative number (-negative index - 1)
				//So if customer would be inserted at index 5, -6 would be returned.
				if (index < 0) {
					//going back to positive values and reducing one for indexing based-0:
				    index = (index * -1) - 1;
				}
				customer.add(c);
			}

		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			log.error("Could not complete query", e);
			throw new RuntimeException(e);
		}

		Database.closeConnections(ps, rs, conn);

		return customer;
	}

	@Override
	public int compareTo(Customer o) {
		return this.customerName.compareTo(o.getCustomerName());
	}




}
