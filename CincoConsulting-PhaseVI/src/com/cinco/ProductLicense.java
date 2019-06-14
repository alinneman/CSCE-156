package com.cinco;

public class ProductLicense extends Product{

	//declaration of the contents of the ProductLicense object

	private double serviceFee;
	private double annualLicenseFee;

	//constructor for ProductLicense object
	public ProductLicense(String productCode, String productType, String productName,
						  double serviceFee, double annualLicenseFee) {
		//"this" keyword to eliminate ambiguities
		super(productCode, productType, productName);
		this.serviceFee = serviceFee;
		this.annualLicenseFee = annualLicenseFee;
	}
	//allows the ProductLicense object to be empty
	public ProductLicense() {

	}

	//Below are all the generated getters and setters
	public double getServiceFee() {
		return serviceFee;
	}

	public void setServiceFee(double serviceFee) {
		this.serviceFee = serviceFee;
	}

	public double getAnnualLicenseFee() {
		return annualLicenseFee;
	}

	public void setAnnualLicenseFee(double annualLicenseFee) {
		this.annualLicenseFee = annualLicenseFee;
	}

}
