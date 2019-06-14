package com.cinco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.log4j.Logger;


public class Invoice implements Comparable<Invoice> {

	//instantiation of constructor objects
	private String invoiceCode;
	private Customer customer;
	private Person salesperson;
	private Map<Product, Double> productMap; //maps product object to its quantity in the invoice

	//instantiation of a logger
	public static Logger log = Logger.getLogger(Invoice.class);

	//creating the invoice object
	public Invoice(String invoiceCode, Customer customer,
				   Person salesperson, Map<Product, Double> productMap) {
		this.invoiceCode = invoiceCode;
		this.customer = customer;
		this.salesperson = salesperson;
		this.productMap = productMap;
	}

	public Invoice() {

	}
	//getters and setters below:
	public String getInvoiceCode() {
		return invoiceCode;
	}

	public static Logger getLog() {
		return log;
	}

	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Person getSalesperson() {
		return salesperson;
	}

	public void setSalesperson(Person salesperson) {
		this.salesperson = salesperson;
	}

	public Map<Product, Double> getProductMap() {
		return productMap;
	}

	public void setProductMap(Map<Product, Double> productMap) {
		this.productMap = productMap;
	}

	//method that retrieves the map of products to quantities using JDBC
	public static Map<Product, Double> getProductMap(int invoiceId) {

		Map<Product, Double> map = new HashMap<>();

		Database.classForName();

		Connection conn = Database.openConnection();

		String query = "select productId, units, hours, dateStart, dateEnd from Purchase where invoiceId = ?;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			//Sets the ? in the query equal to invoiceId:
			ps.setInt(1, invoiceId);
			rs = ps.executeQuery();
			while(rs.next()) {
				int productId = rs.getInt("productId");
				Product p = Product.getProduct(productId);

				if(p.getProductType().contentEquals("E")) {
					double units = rs.getDouble("units");
					map.put(p, units);
				}
				if(p.getProductType().contentEquals("C")) {
					double hours = rs.getDouble("hours");
					map.put(p, hours);
				}
				if(p.getProductType().contentEquals("L")) {
					String dateStart = rs.getString("dateStart");
					String dateEnd = rs.getString("dateEnd");

					//Formatting to accept dates of the form "2000/1/1" rather than only "2000/01/01"
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
					formatter = formatter.withLocale(Locale.US);

					LocalDate start = LocalDate.parse(dateStart, formatter);
					LocalDate end = LocalDate.parse(dateEnd, formatter);

					double days = ChronoUnit.DAYS.between(start, end);
					map.put(p, days);
				}
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

		return map;
	}

	//method that returns a list of invoices using JDBC
	public static CCCList<Invoice> getInvoiceList() {

		CCCList<Invoice> invoice = new CCCList<>();

		Database.classForName();

		Connection conn = Database.openConnection();

		//write a query to get Invoices
		String query = "select invoiceId, invoiceCode, customerId, salespersonId from Invoice;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			//iterates through each row of the sql table to fill the list
			while(rs.next()) {
				int invoiceId = rs.getInt("invoiceId");
				String invoiceCode = rs.getString("invoiceCode");
				int customerId = rs.getInt("customerId");
				int salespersonId = rs.getInt("salespersonId");
				Customer c = Customer.getCustomer(customerId);
				Person p = Person.getPerson(salespersonId);
				Map<Product, Double> map = Invoice.getProductMap(invoiceId);
				Invoice i = new Invoice(invoiceCode, c, p, map);
				invoice.add(i);
			}

		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			log.error("Could not complete query", e);
			throw new RuntimeException(e);
		}

		Database.closeConnections(ps, rs, conn);

		return invoice;
	}

	//method that returns a list of invoices sorted by customer type,
	//then by salesperson last and first name using JDBC
	public static CCCList<Invoice> getInvoiceListTypeSorted() {

		CCCList<Invoice> invoice = new CCCList<>();

		Database.classForName();

		Connection conn = Database.openConnection();

		//write a query to get Invoices
		String query = "select invoiceId, invoiceCode, customerId, salespersonId from Invoice;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			//iterates through each row of the sql table to fill the list
			while(rs.next()) {
				int invoiceId = rs.getInt("invoiceId");
				String invoiceCode = rs.getString("invoiceCode");
				int customerId = rs.getInt("customerId");
				int salespersonId = rs.getInt("salespersonId");
				Customer c = Customer.getCustomer(customerId);
				Person p = Person.getPerson(salespersonId);
				Map<Product, Double> map = Invoice.getProductMap(invoiceId);
				Invoice i = new Invoice(invoiceCode, c, p, map);


				//instantiation of comparator via Java 8 syntax
				Comparator<Invoice> byType = (Invoice i1, Invoice i2) ->
				//comparison made:
				i1.compareTo(i2);

				int index = CCCList.binarySearch(invoice, i, byType);
				//Invoice objects are comparable, so the search can begin:
				//when binary search does not match an index exactly, it returns a
				//negative number (negative index - 1)
				//So if invoice an would be inserted at index 5, -6 would be returned.
				if (index < 0) {
					//going back to positive values and reducing one for indexing based-0:
				    index = (index * -1) - 1;
				}
				invoice.add(index, i);
			}

		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			log.error("Could not complete query", e);
			throw new RuntimeException(e);
		}

		Database.closeConnections(ps, rs, conn);

		return invoice;
	}

	//method that returns a list of invoices using JDBC
	public static CCCList<Invoice> getInvoiceListCostSorted() {

		CCCList<Invoice> invoice = new CCCList<>();

		Database.classForName();

		Connection conn = Database.openConnection();

		//write a query to get Invoices
		String query = "select invoiceId, invoiceCode, customerId, salespersonId from Invoice;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			//iterates through each row of the sql table to fill the list
			while(rs.next()) {
				int invoiceId = rs.getInt("invoiceId");
				String invoiceCode = rs.getString("invoiceCode");
				int customerId = rs.getInt("customerId");
				int salespersonId = rs.getInt("salespersonId");
				Customer c = Customer.getCustomer(customerId);
				Person p = Person.getPerson(salespersonId);
				Map<Product, Double> map = Invoice.getProductMap(invoiceId);
				Invoice i = new Invoice(invoiceCode, c, p, map);

				//instantiation of comparator via Java 8 syntax
				Comparator<Invoice> byCost = (Invoice i1, Invoice i2) ->
				//comparison made:
				(int) (Invoice.getTotal(i2) - Invoice.getTotal(i1));

				int index = CCCList.binarySearch(invoice, i, byCost);
				//Invoice totals are comparable, so the search can begin:
				//when binary search does not match an index exactly, it returns a
				//negative number (negative index - 1)
				//So if invoice an would be inserted at index 5, -6 would be returned.
				if (index < 0) {
					//going back to positive values and reducing one for indexing based-0:
				    index = (index * -1) - 1;
				}
				invoice.add(index, i);
			}

		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			log.error("Could not complete query", e);
			throw new RuntimeException(e);
		}

		Database.closeConnections(ps, rs, conn);

		return invoice;
	}

	//method that returns a list of invoices using JDBC
	public static CCCList<Invoice> getInvoiceListCustomerSorted() {

		CCCList<Invoice> invoice = new CCCList<>();


		Database.classForName();

		Connection conn = Database.openConnection();

		//write a query to get Invoices
		String query = "select invoiceId, invoiceCode, customerId, salespersonId from Invoice;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			//iterates through each row of the sql table to fill the list
			while(rs.next()) {
				int invoiceId = rs.getInt("invoiceId");
				String invoiceCode = rs.getString("invoiceCode");
				int customerId = rs.getInt("customerId");
				int salespersonId = rs.getInt("salespersonId");
				Customer c = Customer.getCustomer(customerId);
				Person p = Person.getPerson(salespersonId);
				Map<Product, Double> map = Invoice.getProductMap(invoiceId);
				Invoice i = new Invoice(invoiceCode, c, p, map);

				//instantiation of comparator via Java 8 syntax
				Comparator<Invoice> byCustomer = (Invoice i1, Invoice i2) ->
				//comparison made:
				i1.getCustomer().compareTo(i2.getCustomer());

				int index = CCCList.binarySearch(invoice, i, byCustomer);
				//Customer objects are comparable, so the search can begin:
				//when binary search does not match an index exactly, it returns a
				//negative number (negative index - 1)
				//So if invoice an would be inserted at index 5, -6 would be returned.
				if (index < 0) {
					//going back to positive values and reducing one for indexing based-0:
				    index = (index * -1) - 1;
				}
				invoice.add(index, i);

			}

		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			log.error("Could not complete query", e);
			throw new RuntimeException(e);
		}

		Database.closeConnections(ps, rs, conn);

		return invoice;
	}

	//method to determine if a customer is charged a compliance fee and assigns that fee
	public static double getComplianceFee(Invoice i) {
		double complianceFee = 0;

		if (i.getCustomer().getType().contentEquals("G")) {
			complianceFee = 125;
		}
		return complianceFee;
	}

	//method to return a list of costs (essentially quantity * price)
	public static CCCList<Double> getCostList(Invoice i) {
		double cost = 0;
		CCCList<Double> costList = new CCCList<>();

		//utilizes Map.Entry to iterate through each map entry
		for (Entry<Product, Double> entry : i.getProductMap().entrySet()) {
			//searching for equipment:
			if (entry.getKey().getProductType().contentEquals("E")) {
	            cost = ((ProductEquipment) entry.getKey()).getPricePerUnit() * entry.getValue();
	            costList.add(cost);
			 }
			//searching for consultation
		 	if (entry.getKey().getProductType().contentEquals("C")) {
	            cost = ((ProductConsultation) entry.getKey()).getHourlyFee() * entry.getValue();
	            costList.add(cost);
		 	}
		 	//searching for license
		 	if (entry.getKey().getProductType().contentEquals("L")) {
	            cost = ((ProductLicense) entry.getKey()).getAnnualLicenseFee() * (entry.getValue()/365);
	            costList.add(cost);
		 	}
		}

		return costList;
	}

	//method that returns the total subtotal of all invoices in the list
	public static double getBatchSubtotal(CCCList<Invoice> invoiceList) {
		double subtotal = 0;

		for(Invoice i : invoiceList) {

			subtotal += Invoice.getTotalCost(i);
		}
		return subtotal;
	}

	//method that returns the total fees of all invoices in the list
	public static double getBatchFees(CCCList<Invoice> invoiceList) {
		double fees = 0;

		for(Invoice i : invoiceList) {

			fees += Invoice.getTotalFees(i);
		}
		return fees;
	}

	//method that returns the total taxes of all invoices in the list
	public static double getBatchTaxes(CCCList<Invoice> invoiceList) {
		double taxes = 0;

		for(Invoice i : invoiceList) {

			taxes += Invoice.getTaxTotal(i);
		}
		return taxes;
	}

	//method that returns the overall total of all invoices in the list
	public static double getBatchTotal(CCCList<Invoice> invoiceList) {
		double total = 0;

		for(Invoice i : invoiceList) {
			total += Invoice.getTotal(i);
		}
		return total;
	}

	//method that returns the itemized product report as a string
	//iterate through set of all entries for each invoice to itemize an entire invoice
	public static String getProductReport(Entry<Product, Double> entry) {
		//productInfo uniformly prints the quantity and price for each product in the invoice
		//report combines productInfo with the corresponding product code, fees, and cost
		String report = new String("");
		if (entry.getKey().getProductType().contentEquals("E")) {
			String productInfo = String.format("%s (%s units @ $%.2f/unit)",
											   entry.getKey().getProductName(),
											   entry.getValue(), ((ProductEquipment)
											   entry.getKey()).getPricePerUnit());
			report = String.format("%-10s%-85s$%-13.2f$%1.2f\n ",
								   entry.getKey().getProductCode(),
								   productInfo,
								   0.0,
								   Invoice.getCost(entry));
		}
		if (entry.getKey().getProductType().contentEquals("C")) {
			String productInfo = String.format("%s (%s hrs @ $%.2f/hour)",
											   entry.getKey().getProductName(),
											   entry.getValue(), ((ProductConsultation)
											   entry.getKey()).getHourlyFee());
			report = String.format("%-10s%-85s$%-13.2f$%1.2f\n ",
								   entry.getKey().getProductCode(),
								   productInfo,
								   150.0,
								   Invoice.getCost(entry));
		}
		if (entry.getKey().getProductType().contentEquals("L")) {
			String productInfo = String.format("%s (%1.0f days @ $%1.2f/yr)",
											   entry.getKey().getProductName(),
											   entry.getValue(), ((ProductLicense)
											   entry.getKey()).getAnnualLicenseFee());
			report = String.format("%-10s%-85s$%-13.2f$%1.2f\n ",
								   entry.getKey().getProductCode(),
								   productInfo, ((ProductLicense)
								   entry.getKey()).getServiceFee(),
								   Invoice.getCost(entry));
		}
		return report;
	}

	//method to return a list of fees attached to each product in an invoice
	public static CCCList<Double> getFeesList(Invoice i) {
		CCCList<Double> feesList = new CCCList<>();
		double fees = 0;

		for (Product p : i.getProductMap().keySet()) {
			if (p.getProductType().contentEquals("L")) {
				fees = ((ProductLicense) p).getServiceFee();
				feesList.add(fees);

			}
			if (p.getProductType().contentEquals("E")) {
				fees = 0;
				feesList.add(fees);

			}
			if (p.getProductType().contentEquals("C")) {
				fees = 150;
				feesList.add(fees);

			}
		}
		return feesList;
	}

	//method that returns the sum of all fees for an invoice (including compliance fee)
	public static double getTotalFees(Invoice i) {
		double total = 0;
		CCCList<Double> feesList = Invoice.getFeesList(i);
		for (double j : feesList) {
			total += j;
		}

		return total + Invoice.getComplianceFee(i);
	}

	//method that returns the sum of the cost for an invoice
	public static double getTotalCost(Invoice i) {
		double total = 0;
		CCCList<Double> costList = Invoice.getCostList(i);
		for (double j : costList) {
			total += j;
		}
		return total;
	}

	//method that returns the sum of all taxes paid for an invoice (if applicable)
	public static double getTaxTotal(Invoice i) {
		double total = 0;
		double taxes = 0;
		double cost = 0;

		if (Invoice.getComplianceFee(i) == 0) {
			for (Entry<Product, Double> entry : i.getProductMap().entrySet()) {
				//equipment has a 7% tax rate
				if (entry.getKey().getProductType().contentEquals("E")) {
	            	cost = ((ProductEquipment) entry.getKey()).getPricePerUnit() * entry.getValue();
	            	taxes = cost * 0.07;
			 	}
				//consultations and licenses have a 4.25% tax rate
		 		if (entry.getKey().getProductType().contentEquals("C")) {
		 			cost = ((ProductConsultation) entry.getKey()).getHourlyFee() * entry.getValue();
	            	taxes = cost * 0.0425;
		 		}
		 		if (entry.getKey().getProductType().contentEquals("L")) {
		 			cost = ((ProductLicense) entry.getKey()).getAnnualLicenseFee() * (entry.getValue()/365);
		 			taxes = cost * 0.0425;
		 		}
				total += taxes;
			}

		}
		return total;
	}

	//method that returns the sum of the costs, taxes, and fees for an invoice
	public static double getTotal(Invoice i) {
		double total = Invoice.getTaxTotal(i) +
					   Invoice.getTotalCost(i) +
					   Invoice.getTotalFees(i);
		return total;
	}

	//method that returns the cost for a given map entry (useful for iteration through entry set)
	public static double getCost(Entry<Product, Double> entry) {
		double cost = 0;
		if (entry.getKey().getProductType().contentEquals("E")) {
           	cost = ((ProductEquipment) entry.getKey()).getPricePerUnit() * entry.getValue();
	 	}
 		if (entry.getKey().getProductType().contentEquals("C")) {
 			cost = ((ProductConsultation) entry.getKey()).getHourlyFee() * entry.getValue();
	 	}
	 	if (entry.getKey().getProductType().contentEquals("L")) {
			cost = ((ProductLicense) entry.getKey()).getAnnualLicenseFee() * (entry.getValue()/365);
		}
		return cost;
	}

	//overriding the compareTo method:
	@Override
	public int compareTo(Invoice o) {
		//compares the customer types
		int typeDif = this.customer.getType().compareTo(o.getCustomer().getType());
		//if there is a difference in customer types, the comparison is made there
		if(typeDif != 0) {
			return typeDif;
		}
		//if there is no difference in customer types, we move to salesperson's last name:
		int lastNameDif = this.salesperson.getLastName().compareTo(o.getSalesperson().getLastName());
		//if there is a difference in last names, the comparison is made there
		if(lastNameDif != 0) {
			return lastNameDif;
		}
		//if there is no difference in last names, we make a comparison on first names:
		return this.salesperson.getFirstName().compareTo(o.getSalesperson().getFirstName());
	}

}
