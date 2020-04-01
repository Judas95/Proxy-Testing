package com.odoojava.api.testing;

public class Factura {

	private int invoiceId;
	private float untaxedAmount;
	private float taxAmount;
	private float totalAmount;
	private Cliente client;
	
	
	public Factura(int invoiceId,  float untaxedAmount, float taxAmount, float totalAmount,Cliente client) {
		super();
		this.invoiceId = invoiceId;
		this.totalAmount = totalAmount;
		this.taxAmount = taxAmount;
		this.client = client;
		this.untaxedAmount = untaxedAmount;
	}


	public int getInvoiceId() {
		return invoiceId;
	}


	public float getUntaxedAmount() {
		return untaxedAmount;
	}


	public void setUntaxedAmount(float untaxedAmount) {
		this.untaxedAmount = untaxedAmount;
	}


	public float getTaxAmount() {
		return taxAmount;
	}


	public void setTaxAmount(float taxAmount) {
		this.taxAmount = taxAmount;
	}


	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}


	public float getTotalAmount() {
		return totalAmount;
	}


	public void setTotalAmount(float totalAmount) {
		this.totalAmount = totalAmount;
	}



	public Cliente getClient() {
		return client;
	}


	public void setClient(Cliente client) {
		this.client = client;
	}

	
}
