/*
 * Alex Linneman
 * CSCE 156
 * April 19, 2019
 *
 * In this program, I will read in the product, persons, customer, and invoice data from
 * files. This data will then be parsed and put into lists. This will be done in the form
 * of a method so that the method can be called upon later for the lists of data.
 * From Phase 1 of the Cinco Consulting project. There now exists functionality to read in
 * data from files as well as from a database.
 */

package com.cinco;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class DataReader {


	private static Scanner customerData; //Calls a scanner to read in Customers.dat
	private static Scanner personsData; //Calls a scanner to read in Persons.dat
	private static Scanner productData; //Calls a scanner to read in Products.dat
	private static Scanner invoiceData; //Calls a scanner to read in Invoice.dat


	// method for persons:
	public static List<Person> getPersonsData(String file){
		List<Person> personsList = new ArrayList<>();

		//standard reading in procedure:
		try {
		personsData = new Scanner(new File("data/Persons.dat"));
		} catch (FileNotFoundException e) {
		e.printStackTrace();
		}
		personsData.nextLine();

		while (personsData.hasNext()) {
			String line = personsData.nextLine();
			String[] personsTokens = line.split(";");
			String[] nameTokens = personsTokens[1].split(",");

			//tokenizing the address in persons data
			String addressTokens[] = personsTokens[2].split(",");

			//filling the address object
			Address a = new Address(addressTokens[0],
									addressTokens[1],
									addressTokens[2],
									addressTokens[3],
									addressTokens[4]);
			List<String> email = new ArrayList<>();

 			//if length is 4 then an email is present
			if (personsTokens.length == 4){
				if(personsTokens[3].contains(",")) {
					String[] emailTokens = personsTokens[3].split(",");
					for(String s : emailTokens) {
						email.add(s);
					}
				}
				else {
					email.add(personsTokens[3]);
				}
				Person p = new Person(personsTokens[0], nameTokens[0], nameTokens[1], a, email);
				personsList.add(p);
			}
			else {
				Person p = new Person(personsTokens[0], nameTokens[0], nameTokens[1], a);
				personsList.add(p);
			}
		}
		personsData.close();
		return personsList;
	}
	//same procedure as above but for customers:
	public static List<Customer> getCustomerData(String file){
		List<Customer> customerList = new ArrayList<>();
		List<Customer> sorted = new ArrayList<>();

		try {
			customerData = new Scanner(new File(file));
			customerData.nextLine(); //skips the first line because it only reads a number


			while (customerData.hasNext()) { //while loop to read in every line of the file
				Customer c = new Customer();
				String line = customerData.nextLine(); //tells the loop to move to the next line each iteration
				String [] customerTokens = line.split(";"); //delimits on ";"

				//tokenizing the address in persons data
				String addressTokens[] = customerTokens[4].split(",");
				//filling the address object
				Address a = new Address(addressTokens[0],
										addressTokens[1],
										addressTokens[2],
										addressTokens[3],
										addressTokens[4]);

				List<Person> personList = DataReader.getPersonsData("data/Persons.dat");

				c.setCustomerCode(customerTokens[0]); //adding in customer code to customer object
				c.setType(customerTokens[1]); //adding in customer type to customer object
				c.setCustomerName(customerTokens[3]); //adding in customer name to customer object
				c.setCustomerAddress(a); //adding in customer address to customer object



				for(Person j : personList) {
					if(customerTokens[2].contains(j.getPersonCode())) {
						c.setPrimaryContact(j);
						break;
					}
				}

				int index = Collections.binarySearch(sorted, c);
				if (index < 0) {
				    index = (index * -1) - 1;
				}

				sorted.add(index, c);

				customerList.add(c);

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		customerData.close(); //closes the scanner
		return sorted;
	}
	//same procedure as above but for products:
	public static ArrayList<Product> getProductData(String file) {

		ArrayList<Product> productList = new ArrayList<>();

		try {
			productData = new Scanner(new File("data/Products.dat"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		productData.nextLine();

		String line = null;
		while(productData.hasNext()) {
			line = productData.nextLine();
			String[] productTokens = line.split(";");

			// if statement looking for product licenses through an "L" in the second token
			if (productTokens[1].contains("L") || productTokens[1].contains("l")) {
				ProductLicense l = new ProductLicense(productTokens[0],
													  productTokens[1],
													  productTokens[2],
													  Double.parseDouble(productTokens[3]),
													  Double.parseDouble(productTokens[4]));
				productList.add(l);
			}

			// if statement looking for product equipment through an "E" in the second token
			if (productTokens[1].contains("E") || productTokens[1].contains("e")) {
				ProductEquipment e = new ProductEquipment(productTokens[0],
														  productTokens[1],
														  productTokens[2],
														  Double.parseDouble(productTokens[3]));
				productList.add(e);
			}

			//producing the person list to be used to fill the consultant person object:
			List<Person> personList = DataReader.getPersonsData("data/Persons.dat");
			// if statement looking for product consultations through a "C" in the second token
			if (productTokens[1].contains("C") || productTokens[1].contains("c")) {
				for(Person j : personList) {
					if(productTokens[3].contains(j.getPersonCode())) {

						ProductConsultation c = new ProductConsultation(productTokens[0],
																		productTokens[1],
																		productTokens[2],
																		Double.parseDouble(productTokens[4]),
																		j);
						productList.add(c);
						break;
					}
				}
			}

		}
		productData.close();
		return productList;
	}
	//same procedure as above but for invoices
	public static List<Invoice> getInvoiceData(String file) {

		ArrayList<Invoice> invoiceList = new ArrayList<>();

		try {
			invoiceData = new Scanner(new File("data/Invoices.dat"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		invoiceData.nextLine();

		String line = null;
		while(invoiceData.hasNext()) {

			//calling methods to produce person, product, and customer lists to fill
			//the corresponding objects within the invoice
			List<Person> personList = DataReader.getPersonsData("data/Persons.dat");
			List<Product> productList = DataReader.getProductData("data/Products.dat");
			List<Customer> customerList = DataReader.getCustomerData("data/Customers.dat");

			line = invoiceData.nextLine();

			//this will map products to their specified quantities in each invoice:
			Map<Product, Double> productMap = new HashMap<>();
			String[] invoiceTokens = line.split(";");
			//Each product from the invoice purchase list is separated by commas:
			String[] pList = invoiceTokens[3].split(",");
			//will give a delimited purchase array but needs to be instantiated outside of for loop:
			String[] dPList = null;
			Person person = new Person();
			for (Person p : personList) {
				if (invoiceTokens[2].contentEquals(p.getPersonCode())) {
					person = p;
				}
			}

			for (String s : pList) {
				dPList = s.split(":");
				//We now have a delimited product array to iterate through to fill the map
				//with products and corresponding quantities
				for (Product p : productList) {
					if (dPList[0].contentEquals(p.getProductCode())) {
						//searching for a consultation or consultation because their quantities
						//do not depend on dates
						if (p.getProductType().contentEquals("C") || p.getProductType().contentEquals("E")) {
							productMap.put(p, Double.parseDouble(dPList[1]));
							break;
						}
						//searching for a license
						if (p.getProductType().contentEquals("L")) {
							//getting the start and end dates:
							String dateStart = dPList[1];
							String dateEnd = dPList[2];
							//Formatting to accept dates of the form "2000/1/1" rather than only "2000/01/01"
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
							formatter = formatter.withLocale(Locale.US);
							LocalDate start = LocalDate.parse(dateStart, formatter);
							LocalDate end = LocalDate.parse(dateEnd, formatter);
							double days = ChronoUnit.DAYS.between(start, end);
							productMap.put(p, days);
							break;
						}
					}
				}
			}
			Customer customer = null;
			for (Customer c : customerList) {
				if (invoiceTokens[1].contentEquals(c.getCustomerCode())) {
					customer = c;
				}
			}
			Invoice i = new Invoice(invoiceTokens[0],
									customer,
									person,
									productMap);

			invoiceList.add(i);

		}
		invoiceData.close();
		return invoiceList;
	}
	//returns list of invoices sorted by customer type, then by salesperson name
	public static List<Invoice> getInvoiceDataTypeSorted(String file) {

		ArrayList<Invoice> invoiceList = new ArrayList<>();

		try {
			invoiceData = new Scanner(new File("data/Invoices.dat"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		invoiceData.nextLine();

		String line = null;
		while(invoiceData.hasNext()) {

			//calling methods to produce person, product, and customer lists to fill
			//the corresponding objects within the invoice
			List<Person> personList = DataReader.getPersonsData("data/Persons.dat");
			List<Product> productList = DataReader.getProductData("data/Products.dat");
			List<Customer> customerList = DataReader.getCustomerData("data/Customers.dat");

			line = invoiceData.nextLine();

			//this will map products to their specified quantities in each invoice:
			Map<Product, Double> productMap = new HashMap<>();
			String[] invoiceTokens = line.split(";");
			//Each product from the invoice purchase list is separated by commas:
			String[] pList = invoiceTokens[3].split(",");
			//will give a delimited purchase array but needs to be instantiated outside of for loop:
			String[] dPList = null;
			Person person = new Person();
			for (Person p : personList) {
				if (invoiceTokens[2].contentEquals(p.getPersonCode())) {
					person = p;
				}
			}

			for (String s : pList) {
				dPList = s.split(":");
				//We now have a delimited product array to iterate through to fill the map
				//with products and corresponding quantities
				for (Product p : productList) {
					if (dPList[0].contentEquals(p.getProductCode())) {
						//searching for a consultation or consultation because their quantities
						//do not depend on dates
						if (p.getProductType().contentEquals("C") || p.getProductType().contentEquals("E")) {
							productMap.put(p, Double.parseDouble(dPList[1]));
							break;
						}
						//searching for a license
						if (p.getProductType().contentEquals("L")) {
							//getting the start and end dates:
							String dateStart = dPList[1];
							String dateEnd = dPList[2];
							//Formatting to accept dates of the form "2000/1/1" rather than only "2000/01/01"
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
							formatter = formatter.withLocale(Locale.US);
							LocalDate start = LocalDate.parse(dateStart, formatter);
							LocalDate end = LocalDate.parse(dateEnd, formatter);
							double days = ChronoUnit.DAYS.between(start, end);
							productMap.put(p, days);
							break;
						}
					}
				}
			}
			Customer customer = null;
			for (Customer c : customerList) {
				if (invoiceTokens[1].contentEquals(c.getCustomerCode())) {
					customer = c;
				}
			}
			Invoice i = new Invoice(invoiceTokens[0],
									customer,
									person,
									productMap);

			//Binary search takes a list that extends comparable and a key for
			//input. It then uses those to search for the correct insertion index
			//to produce a sorted list
			int index = Collections.binarySearch(invoiceList, i);
			//Invoice objects extend comparable, so the search can begin:
			//when binary search does not match an index exactly, it returns a
			//negative number (negative index - 1)
			//So if invoice an would be inserted at index 5, -6 would be returned.
			if (index < 0) {
				//going back to positive values and reducing one for indexing based-0:
			    index = (index * -1) - 1;
			}
			invoiceList.add(index, i);

		}
		invoiceData.close();
		return invoiceList;
	}
	//returns list of invoices sorted by customer name
	public static List<Invoice> getInvoiceDataCustomerSorted(String file) {

		ArrayList<Invoice> invoiceList = new ArrayList<>();

		try {
			invoiceData = new Scanner(new File("data/Invoices.dat"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		invoiceData.nextLine();

		String line = null;
		while(invoiceData.hasNext()) {

			//calling methods to produce person, product, and customer lists to fill
			//the corresponding objects within the invoice
			List<Person> personList = DataReader.getPersonsData("data/Persons.dat");
			List<Product> productList = DataReader.getProductData("data/Products.dat");
			List<Customer> customerList = DataReader.getCustomerData("data/Customers.dat");

			line = invoiceData.nextLine();

			//this will map products to their specified quantities in each invoice:
			Map<Product, Double> productMap = new HashMap<>();
			String[] invoiceTokens = line.split(";");
			//Each product from the invoice purchase list is separated by commas:
			String[] pList = invoiceTokens[3].split(",");
			//will give a delimited purchase array but needs to be instantiated outside of for loop:
			String[] dPList = null;
			Person person = new Person();
			for (Person p : personList) {
				if (invoiceTokens[2].contentEquals(p.getPersonCode())) {
					person = p;
				}
			}

			for (String s : pList) {
				dPList = s.split(":");
				//We now have a delimited product array to iterate through to fill the map
				//with products and corresponding quantities
				for (Product p : productList) {
					if (dPList[0].contentEquals(p.getProductCode())) {
						//searching for a consultation or consultation because their quantities
						//do not depend on dates
						if (p.getProductType().contentEquals("C") || p.getProductType().contentEquals("E")) {
							productMap.put(p, Double.parseDouble(dPList[1]));
							break;
						}
						//searching for a license
						if (p.getProductType().contentEquals("L")) {
							//getting the start and end dates:
							String dateStart = dPList[1];
							String dateEnd = dPList[2];
							//Formatting to accept dates of the form "2000/1/1" rather than only "2000/01/01"
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
							formatter = formatter.withLocale(Locale.US);
							LocalDate start = LocalDate.parse(dateStart, formatter);
							LocalDate end = LocalDate.parse(dateEnd, formatter);
							double days = ChronoUnit.DAYS.between(start, end);
							productMap.put(p, days);
							break;
						}
					}
				}
			}
			Customer customer = null;
			for (Customer c : customerList) {
				if (invoiceTokens[1].contentEquals(c.getCustomerCode())) {
					customer = c;
				}
			}
			Invoice i = new Invoice(invoiceTokens[0],
									customer,
									person,
									productMap);

			//instantiation of comparator via Java 8 syntax
			Comparator<Invoice> byCustomer = (Invoice i1, Invoice i2) ->
			//comparison made:
			i1.getCustomer().compareTo(i2.getCustomer());

			//Binary search takes a list that extends comparable, a key, and a comparator
			//for input. It then uses those to search for the correct insertion index
			//to produce a sorted list
			int index = Collections.binarySearch(invoiceList, i, byCustomer);
			//Invoice objects extend comparable, so the search can begin:
			//when binary search does not match an index exactly, it returns a
			//negative number (negative index - 1)
			//So if invoice an would be inserted at index 5, -6 would be returned.
			if (index < 0) {
				//going back to positive values and reducing one for indexing based-0:
			    index = (index * -1) - 1;
			}
			invoiceList.add(index, i);

		}
		invoiceData.close();
		return invoiceList;
	}
	//returns list of invoices sorted by invoice cost
	public static List<Invoice> getInvoiceDataCostSorted(String file) {

		ArrayList<Invoice> invoiceList = new ArrayList<>();

		try {
			invoiceData = new Scanner(new File("data/Invoices.dat"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		invoiceData.nextLine();

		String line = null;
		while(invoiceData.hasNext()) {

			//calling methods to produce person, product, and customer lists to fill
			//the corresponding objects within the invoice
			List<Person> personList = DataReader.getPersonsData("data/Persons.dat");
			List<Product> productList = DataReader.getProductData("data/Products.dat");
			List<Customer> customerList = DataReader.getCustomerData("data/Customers.dat");

			line = invoiceData.nextLine();

			//this will map products to their specified quantities in each invoice:
			Map<Product, Double> productMap = new HashMap<>();
			String[] invoiceTokens = line.split(";");
			//Each product from the invoice purchase list is separated by commas:
			String[] pList = invoiceTokens[3].split(",");
			//will give a delimited purchase array but needs to be instantiated outside of for loop:
			String[] dPList = null;
			Person person = new Person();
			for (Person p : personList) {
				if (invoiceTokens[2].contentEquals(p.getPersonCode())) {
					person = p;
				}
			}

			for (String s : pList) {
				dPList = s.split(":");
				//We now have a delimited product array to iterate through to fill the map
				//with products and corresponding quantities
				for (Product p : productList) {
					if (dPList[0].contentEquals(p.getProductCode())) {
						//searching for a consultation or consultation because their quantities
						//do not depend on dates
						if (p.getProductType().contentEquals("C") || p.getProductType().contentEquals("E")) {
							productMap.put(p, Double.parseDouble(dPList[1]));
							break;
						}
						//searching for a license
						if (p.getProductType().contentEquals("L")) {
							//getting the start and end dates:
							String dateStart = dPList[1];
							String dateEnd = dPList[2];
							//Formatting to accept dates of the form "2000/1/1" rather than only "2000/01/01"
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
							formatter = formatter.withLocale(Locale.US);
							LocalDate start = LocalDate.parse(dateStart, formatter);
							LocalDate end = LocalDate.parse(dateEnd, formatter);
							double days = ChronoUnit.DAYS.between(start, end);
							productMap.put(p, days);
							break;
						}
					}
				}
			}
			Customer customer = null;
			for (Customer c : customerList) {
				if (invoiceTokens[1].contentEquals(c.getCustomerCode())) {
					customer = c;
				}
			}
			Invoice i = new Invoice(invoiceTokens[0],
									customer,
									person,
									productMap);

			//instantiation of comparator via Java 8 syntax
			Comparator<Invoice> byCost = (Invoice i1, Invoice i2) ->
			//comparison made:
			(int) (Invoice.getTotal(i2) - Invoice.getTotal(i1));

			//Binary search takes a list that extends comparable, a key, and a comparator
			//for input. It then uses those to search for the correct insertion index
			//to produce a sorted list
			int index = Collections.binarySearch(invoiceList, i, byCost);
			//Invoice objects extend comparable, so the search can begin:
			//when binary search does not match an index exactly, it returns a
			//negative number (negative index - 1)
			//So if invoice an would be inserted at index 5, -6 would be returned.
			if (index < 0) {
				//going back to positive values and reducing one for indexing based-0:
			    index = (index * -1) - 1;
			}
			invoiceList.add(index, i);

		}
		invoiceData.close();
		return invoiceList;
	}

	}