/*
 * Alex Linneman
 * CSCE 156
 * April 19, 2019
 *
 * This is the data converter class from Phase I of the Cinco Consulting project.
 * In this class, I will read in data for customers, persons, and products.
 * Then, I will split them and store them into their designated objects from
 * their respective classes. From there, the objects will be converted to a json
 * format and the result will be printed as an output.
 */

package com.cinco;

import java.util.ArrayList;



import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class DataConverter {

	public static void main(String[] args) {

		//calls the method returns a list of customers:
		List<Customer> customerList = DataReader.getCustomerData("data/Customers.dat");

		try {
		//instantiation of a PrintWriter used to write a .json file for customers:
		PrintWriter customerOut = new PrintWriter("data/Customers.json");

		//iteration through list of customers to convert each customer object to json
		for(Customer i : customerList) {
		Gson customerGson = new GsonBuilder().setPrettyPrinting().create();
		String customerJson = customerGson.toJson(i);

		customerOut.println(customerJson);

		}
		customerOut.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

		ArrayList<Product> productList = DataReader.getProductData("data/Products.dat");

		try {

		//instantiation of a PrintWriter used to write a .json file for products:
		PrintWriter productOut = new PrintWriter("data/Product.json");

		//iteration through list of product to convert each product object to json
		for(Product i : productList) {
		Gson productGson = new GsonBuilder().setPrettyPrinting().create();
		String productJson = productGson.toJson(i);

		productOut.println(productJson);

		}

		productOut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<Person> personList = DataReader.getPersonsData("data/Persons.dat");

		try {

		//instantiation of a PrintWriter used to write a .json file for persons:
		PrintWriter personOut = new PrintWriter("data/Persons.json");

		//iteration through list of persons to convert each person object to json
		for(Person i : personList) {
		Gson personGson = new GsonBuilder().setPrettyPrinting().create();
		String personJson = personGson.toJson(i);

		personOut.println(personJson);

		}
		personOut.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

		List<Invoice> invoiceList = DataReader.getInvoiceData("data/Invoices.dat");

		try {

		//instantiation of a PrintWriter used to write a .json file for customers:
		PrintWriter invoiceOut = new PrintWriter("data/Invoice.json");

		//iteration through list of invoices to convert each invoice object to json
		for(Invoice i : invoiceList) {

			Gson invoiceGson = new GsonBuilder().setPrettyPrinting().create();
			String invoiceJson = invoiceGson.toJson(i);

			invoiceOut.println(invoiceJson);

		}
		invoiceOut.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

		}

}

