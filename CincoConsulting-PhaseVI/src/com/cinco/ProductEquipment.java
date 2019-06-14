package com.cinco;

public class ProductEquipment extends Product {

	//declaration of the contents of the ProductEquipment object
	private double pricePerUnit;

	//constructor for ProductEquipment object
	public ProductEquipment(String productCode, String productType,
							String productName, double pricePerUnit) {
		//"this" keyword to eliminate ambiguities
		super(productCode, productType, productName);
		this.pricePerUnit = pricePerUnit;
	}
	//allows the ProductEquipment object to be empty
	public ProductEquipment() {
	}

	//Below are all the generated getters and setters
	public double getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

}
