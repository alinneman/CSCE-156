package com.cinco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class Product {

	//declaration of the contents of the Product object
	private String productCode;
	private String productType;
	private String productName;

	//instantiation of a logger
	public static Logger log = Logger.getLogger(Invoice.class);

	//constructor for Product object
	public Product(String productCode, String productType, String productName) {

		//"this" keyword to eliminate ambiguities
		this.productCode = productCode;
		this.productType = productType;
		this.productName = productName;
	}
	public Product() {

	}

	//below are getters and setters
	public String getProductCode() {
		return productCode;
	}
	public static Logger getLog() {
		return log;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}

	//method that returns the product that corresponds to the given productId using JDBC
	public static Product getProduct(int productId) {
		Product p = new Product();

		Database.classForName();

		Connection conn = Database.openConnection();

		String query = "select productCode, "
					 + "productType, "
					 + "productName, "
					 + "pricePerUnit, "
					 + "serviceFee, "
					 + "annualLicenseFee, "
					 + "consultantPersonId, "
					 + "hourlyFee "
					 + "from Product where productId = ?;";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, productId);
			rs = ps.executeQuery();
			if(rs.next()) {
				String productCode = rs.getString("productCode");
				String productType = rs.getString("productType");
				String productName = rs.getString("productName");

				if(productType.equals("E")) {
					double pricePerUnit = rs.getDouble("pricePerUnit");
					p = new ProductEquipment(productCode, productType, productName, pricePerUnit);
				}
				if(productType.equals("L")) {
					double serviceFee = rs.getDouble("serviceFee");
					double annualLicenseFee = rs.getDouble("annualLicenseFee");
					p = new ProductLicense(productCode, productType, productName, serviceFee, annualLicenseFee);
				}
				if(productType.equals("C")) {
					int consultantPersonId = rs.getInt("consultantPersonId");
					double hourlyFee = rs.getDouble("hourlyFee");
					Person person = Person.getPerson(consultantPersonId);
					p = new ProductConsultation(productCode, productType, productName, hourlyFee, person);
				}
			} else {
				throw new IllegalStateException("No product in database with id = " + productId);
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

	//method that returns a list of products using JDBC
	public static List<Product> getProductList() {

		List<Product> product = new ArrayList<>();

		Database.classForName();

		Connection conn = Database.openConnection();

		String query = "select productCode, productType, productName, serviceFee, annualLicenseFee from Product where productType = 'L';";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()) {
				String productCode = rs.getString("productCode");
				String productType = rs.getString("productType");
				String productName = rs.getString("productName");
				double serviceFee = rs.getDouble("serviceFee");
				double annualLicenseFee = rs.getDouble("annualLicenseFee");
				ProductLicense l = new ProductLicense(productCode, productType, productName, serviceFee, annualLicenseFee);
				product.add(l);
			}


		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			log.error("Could not complete query", e);
			throw new RuntimeException(e);
		}

		String eQuery = "select productCode, productType, productName, pricePerUnit from Product where productType = 'E';";

		try {
			ps = conn.prepareStatement(eQuery);
			rs = ps.executeQuery();
			while(rs.next()) {
				String productCode = rs.getString("productCode");
				String productType = rs.getString("productType");
				String productName = rs.getString("productName");
				double pricePerUnit = rs.getDouble("pricePerUnit");
				ProductEquipment e = new ProductEquipment(productCode, productType, productName, pricePerUnit);
				product.add(e);
			}
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			log.error("Could not complete query", e);
			throw new RuntimeException(e);
		}

		String cQuery = "select productCode, productType, productName, consultantPersonId, hourlyFee from Product where productType = 'C';";

		try {
			ps = conn.prepareStatement(cQuery);
			rs = ps.executeQuery();
			while(rs.next()) {
				String productCode = rs.getString("productCode");
				String productType = rs.getString("productType");
				String productName = rs.getString("productName");
				int consultantPersonId = rs.getInt("consultantPersonId");
				double hourlyFee = rs.getDouble("hourlyFee");
				Person p = Person.getPerson(consultantPersonId);
				ProductConsultation c = new ProductConsultation(productCode, productType, productName, hourlyFee, p);
				product.add(c);
			}

		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			log.error("Could not complete query", e);
			throw new RuntimeException(e);
		}

		Database.closeConnections(ps, rs, conn);

		return product;
	}

}