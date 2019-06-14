package com.cinco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


public class Person {

	//declaration of the contents of the Person object
	private String personCode;
	private String lastName;
	private String firstName;
	private Address personAddress;
	private List<String> email;

	//instantiation of a logger
	public static Logger log = Logger.getLogger(Person.class);

	//constructor for Person object
	public Person(String personCode, String lastName, String firstName,
				  Address personAddress, List<String> email) {
		//"this" keyword to eliminate ambiguities
		this.personCode = personCode;
		this.lastName = lastName;
		this.firstName = firstName;
		this.personAddress = personAddress;
		this.email = email;
	}

	public Person(String personCode, String lastName,
			  	  String firstName, Address personAddress) {
	//"this" keyword to eliminate ambiguities
	this.personCode = personCode;
	this.lastName = lastName;
	this.firstName = firstName;
	this.personAddress = personAddress;
	}
	//allows the Person object to be empty
	public Person() {

	}
	//Below are all the generated getters and setters
	public String getPersonCode() {
		return personCode;
	}
	public static Logger getLog() {
		return log;
	}
	public List<String> getEmail() {
		return email;
	}
	public void setEmail(List<String> email) {
		this.email = email;
	}
	public void setPersonCode(String personCode) {
		this.personCode = personCode;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public Address getPersonAddress() {
		return personAddress;
	}

	public void setPersonAddress(Address personAddress) {
		this.personAddress = personAddress;
	}

	public static List<String> getEmailList(int personId){
		List<String> emailList = new ArrayList<>();

		Database.classForName();

		Connection conn = Database.openConnection();

		String query = "select email from Email where personId = ?;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			//Sets the ? in the query equal to personId:
			ps.setInt(1, personId);
			rs = ps.executeQuery();
			while(rs.next()) {
				String email = rs.getString("email");
				emailList.add(email);
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

		return emailList;
	}

	//method that returns the person that corresponds to the given personId using JDBC
	public static Person getPerson(int personId) {
		Person p = new Person();

		Database.classForName();

		Connection conn = Database.openConnection();

		String query = "select p.personCode as personCode, "
					 + "p.lastName as lastName, "
					 + "p.firstName as firstName, "
					 + "p.addressId as addressId, "
					 + "e.email as personEmail "
					 + "from Person p left join Email e "
					 + "on p.personId = e.personId where p.personId = ?;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, personId);
			rs = ps.executeQuery();
			if(rs.next()) {
				String personCode = rs.getString("personCode");
				String lastName = rs.getString("lastName");
				String firstName = rs.getString("firstName");
				int addressId = rs.getInt("addressId");
				Address a = Address.getAddress(addressId);
				List<String> email = new ArrayList<>();
				if(rs.getString("personEmail") != null) {
					email = Person.getEmailList(personId);
					p = new Person(personCode, lastName, firstName, a, email);
				}
				else {
					p = new Person(personCode, lastName, firstName, a);
				}
			} else {
				throw new IllegalStateException("No person in database with id = " + personId);
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

		return p;
	}

	//method that returns a list of persons using JDBC
	public static List<Person> getPersonList() {
		List<Person> person = new ArrayList<>();

		Database.classForName();

		Connection conn = Database.openConnection();

		String query = "select p.personId as personId,"
							+ "p.personCode as personCode, "
							+ "p.lastName as lastName, "
							+ "p.firstName as firstName, "
							+ "p.addressId as addressId, "
							+ "e.email as personEmail "
							+ "from Person p join Email e "
							+ "on p.personId = e.personId;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()) {
				String personCode = rs.getString("personCode");
				String lastName = rs.getString("lastName");
				String firstName = rs.getString("firstName");
				int addressId = rs.getInt("addressId");
				Address a = Address.getAddress(addressId);
				int personId = rs.getInt("personId");
				List<String> email = Person.getEmailList(personId);

				Person p = new Person(personCode, lastName, firstName, a, email);
				person.add(p);
			}

		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			log.error("Could not complete query", e);
			throw new RuntimeException(e);
		}


		String noEmailQuery = "select personCode, lastName, "
							+ "firstName, addressId from "
							+ "Person p left join Email e on "
							+ "p.personId = e.personId "
							+ "where emailId is null;";


		try {
			ps = conn.prepareStatement(noEmailQuery);
			rs = ps.executeQuery();
			while(rs.next()) {
				String personCode = rs.getString("personCode");
				String lastName = rs.getString("lastName");
				String firstName = rs.getString("firstName");
				int addressId = rs.getInt("addressId");
				Address a = Address.getAddress(addressId);
				Person p = new Person(personCode, lastName, firstName, a);
				person.add(p);
			}

		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			log.error("Could not complete query", e);
			throw new RuntimeException(e);
		}

		Database.closeConnections(ps, rs, conn);

		return person;
	}
}
