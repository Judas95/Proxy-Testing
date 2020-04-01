package com.odoojava.api.testing;

public class Factura {

	private int invoiceId;
	private float totalAmount;
	private int tax;
	private Cliente client;
	
	
	public Factura(int invoiceId, float totalAmount, int tax, Cliente client) {
		super();
		this.invoiceId = invoiceId;
		this.totalAmount = totalAmount;
		this.tax = tax;
		this.client = client;
	}


	public int getInvoiceId() {
		return invoiceId;
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


	public int getTax() {
		return tax;
	}


	public void setTax(int tax) {
		this.tax = tax;
	}


	public Cliente getClient() {
		return client;
	}


	public void setClient(Cliente client) {
		this.client = client;
	}

	
}
