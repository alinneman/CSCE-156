package com.cinco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class Address {

	//objects for address object
	private String street;
	private String city;
	private String state;
	private String zip;
	private String country;

	//instantiation of a logger
	public static Logger log = Logger.getLogger(Address.class);

	//address object
	Address(String street,
			String city,
			String state,
			String zip,
			String country){
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.country = country;

	}
	Address(){

	}
	//Getters and setters
	public String getStreet() {
		return street;
	}
	public static Logger getLog() {
		return log;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}

	//method that returns the address that corresponds to the given addressId using JDBC
	public static Address getAddress(int addressId) {
		Address a = new Address();

		Database.classForName();

		Connection conn = Database.openConnection();

		String query = "select street, city, state, zip, country "
					 + "from Address where addressId = ?";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			//sets the ? equal to addressId:
			ps.setInt(1, addressId);
			rs = ps.executeQuery();
			//checks for an entry with the given addressId:
			if(rs.next()) {
				//if an entry is found, variables are assigned to sql columns entries:
				String street = rs.getString("street");
				String city = rs.getString("city");
				String state = rs.getString("state");
				String zip = rs.getString("zip");
				String country = rs.getString("country");
				a = new Address(street, city, state, zip, country);
			} else {
				throw new IllegalStateException("No address in database with id = " + addressId);
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

		return a;
	}

	//method that returns a list of addresses using JDBC
	//similar procedure as the method above
	public static List<Address> getAddressList() {

		List<Address> address = new ArrayList<>();

		Database.classForName();

		Connection conn = Database.openConnection();

		String query = "select street, city, state, zip, country from Address";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			//iterates through all rows displayed from the query
			while(rs.next()) {
				String street = rs.getString("street");
				String city = rs.getString("city");
				String state = rs.getString("state");
				String zip = rs.getString("zip");
				String country = rs.getString("country");
				Address a = new Address(street, city, state, zip, country);
				address.add(a);
			}

		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			log.error("Could not complete query", e);
			throw new RuntimeException(e);
		}

		Database.closeConnections(ps, rs, conn);

		return address;
	}

}

