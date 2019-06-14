package com.cinco;

public class ProductConsultation extends Product {

	//declaration of the contents of the ProductConsultation object
	private double hourlyFee;
	private Person consultant;

	//constructor for ProductConsultation object
	public ProductConsultation(String productCode, String productType,
							   String productName,
							   double hourlyFee, Person consultant) {
		//"this" keyword to eliminate ambiguities
		super(productCode, productType, productName);
		this.hourlyFee = hourlyFee;
		this.consultant = consultant;

	}
	//allows the ProductConsultation object to be empty
	public ProductConsultation() {

	}

	//Below are all the generated getters and setters
	public Person getConsultant() {
		return consultant;
	}
	public void setConsultant(Person consultant) {
		this.consultant = consultant;
	}
	public double getHourlyFee() {
		return hourlyFee;
	}
	public void setHourlyFee(double hourlyFee) {
		this.hourlyFee = hourlyFee;
	}

}
