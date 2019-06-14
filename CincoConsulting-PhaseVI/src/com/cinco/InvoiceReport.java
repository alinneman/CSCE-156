/*
 * Alex Linneman
 * CSCE 156
 * Assignment 7
 *
 * In this program, I will use the database data to produce a formatted invoice report.
 * The program will call upon various methods in the Invoice class and utilize them
 * to make the program more user-friendly and readable.
 */

package com.cinco;

import java.io.IOException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.FileAppender;
import org.apache.log4j.SimpleLayout;

public class InvoiceReport {

	public static void main(String[] args) {

		//logs exceptions
		BasicConfigurator.configure();

		try {
			//logs exceptions to a file "loggerInfo.txt" in the data folder
			Address.log.addAppender(new FileAppender(new SimpleLayout(), "data/loggerInfo.txt"));
			Customer.log.addAppender(new FileAppender(new SimpleLayout(), "data/loggerInfo.txt"));
			Database.log.addAppender(new FileAppender(new SimpleLayout(), "data/loggerInfo.txt"));
			Invoice.log.addAppender(new FileAppender(new SimpleLayout(), "data/loggerInfo.txt"));
			Person.log.addAppender(new FileAppender(new SimpleLayout(), "data/loggerInfo.txt"));
			Product.log.addAppender(new FileAppender(new SimpleLayout(), "data/loggerInfo.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}


		//List sorted by customer name created from querying database using JDBC:
		CCCList<Invoice> invoiceList = Invoice.getInvoiceListCustomerSorted();

		//beginning the executive summary invoice report:
		System.out.println("BY CUSTOMER NAME");
		System.out.println("Executive Summary Report");
		System.out.println("============================================================="
						 + "==========================================================="
						 + "===========================================================");
		System.out.printf("Invoice\t\tCustomer\t\t\t\t\tSalesperson\t\t\tSubtotal\t\tFees\t\t\tTaxes\t\t\tTotal\n");

		//Beginning iteration through invoice list
		for(Invoice i : invoiceList) {

			//Setting "lastName, firstName" equal to a single string for ease of formatting:
			String name = String.format("%s, %s", i.getSalesperson().getLastName()
												, i.getSalesperson().getFirstName());

			//Prints out each invoice code, customer, salesperson, subtotal, fees, taxes, and total for an invoice
			System.out.printf("%-16s%-48s%-32s$%-23.2f$%-23.2f$%-23.2f$%-1.2f\n",i.getInvoiceCode(),
																				 i.getCustomer().getCustomerName(),
																				 name,
																				 Invoice.getTotalCost(i),
																				 Invoice.getTotalFees(i),
																				 Invoice.getTaxTotal(i),
																				 Invoice.getTotal(i));
		}

		System.out.println("============================================================="
				 		 + "==========================================================="
				 		 + "===========================================================");

		//Prints out the batch totals for all invoice records in the invoice list
		System.out.printf("%-96s$%-23.2f$%-23.2f$%-23.2f$%-23.2f\n\n\n","TOTALS", Invoice.getBatchSubtotal(invoiceList),
																			Invoice.getBatchFees(invoiceList),
																			Invoice.getBatchTaxes(invoiceList),
																			Invoice.getBatchTotal(invoiceList));




		//List sorted by invoice total created from querying database using JDBC:
		CCCList<Invoice> invoiceList2 = Invoice.getInvoiceListCostSorted();

		//beginning the executive summary invoice report:
		System.out.println("BY INVOICE TOTAL");
		System.out.println("Executive Summary Report");
		System.out.println("============================================================="
						 + "==========================================================="
						 + "===========================================================");
		System.out.printf("Invoice\t\tCustomer\t\t\t\t\tSalesperson\t\t\tSubtotal\t\tFees\t\t\tTaxes\t\t\tTotal\n");

		//Beginning iteration through invoice list
		for(Invoice i : invoiceList2) {

			//Setting "lastName, firstName" equal to a single string for ease of formatting:
			String name = String.format("%s, %s", i.getSalesperson().getLastName()
												, i.getSalesperson().getFirstName());

			//Prints out each invoice code, customer, salesperson, subtotal, fees, taxes, and total for an invoice
			System.out.printf("%-16s%-48s%-32s$%-23.2f$%-23.2f$%-23.2f$%-1.2f\n",i.getInvoiceCode(),
																				 i.getCustomer().getCustomerName(),
																				 name,
																				 Invoice.getTotalCost(i),
																				 Invoice.getTotalFees(i),
																				 Invoice.getTaxTotal(i),
																				 Invoice.getTotal(i));
		}

		System.out.println("============================================================="
				 		 + "==========================================================="
				 		 + "===========================================================");

		//Prints out the batch totals for all invoice records in the invoice list
		System.out.printf("%-96s$%-23.2f$%-23.2f$%-23.2f$%-23.2f\n\n\n","TOTALS", Invoice.getBatchSubtotal(invoiceList2),
																			Invoice.getBatchFees(invoiceList2),
																			Invoice.getBatchTaxes(invoiceList2),
																			Invoice.getBatchTotal(invoiceList2));

		//List sorted by customer type, then salesperson name created from querying database using JDBC:
		CCCList<Invoice> invoiceList3 = Invoice.getInvoiceListTypeSorted();

		//beginning the executive summary invoice report:
		System.out.println("BY CUSTOMER TYPE, THEN SALESPERSON");
		System.out.println("Executive Summary Report");
		System.out.println("============================================================="
						 + "==========================================================="
						 + "===========================================================");
		System.out.printf("Invoice\t\tCustomer\t\t\t\t\tSalesperson\t\t\tSubtotal\t\tFees\t\t\tTaxes\t\t\tTotal\n");

		//Beginning iteration through invoice list
		for(Invoice i : invoiceList3) {

			//Setting "lastName, firstName" equal to a single string for ease of formatting:
			String name = String.format("%s, %s", i.getSalesperson().getLastName()
												, i.getSalesperson().getFirstName());

			//Prints out each invoice code, customer, salesperson, subtotal, fees, taxes, and total for an invoice
			System.out.printf("%-16s%-48s%-32s$%-23.2f$%-23.2f$%-23.2f$%-1.2f\n",i.getInvoiceCode(),
																				 i.getCustomer().getCustomerName(),
																				 name,
																				 Invoice.getTotalCost(i),
																				 Invoice.getTotalFees(i),
																				 Invoice.getTaxTotal(i),
																				 Invoice.getTotal(i));
		}

		System.out.println("============================================================="
				 		 + "==========================================================="
				 		 + "===========================================================");

		//Prints out the batch totals for all invoice records in the invoice list
		System.out.printf("%-96s$%-23.2f$%-23.2f$%-23.2f$%-23.2f\n\n\n","TOTALS", Invoice.getBatchSubtotal(invoiceList3),
																			Invoice.getBatchFees(invoiceList3),
																			Invoice.getBatchTaxes(invoiceList3),
																			Invoice.getBatchTotal(invoiceList3));


		Product x = new ProductLicense();
	}
}