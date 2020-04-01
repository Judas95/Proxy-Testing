package com.odoojava.api.testing;

public class Factura {

	private int invoiceId;
	private float untaxedAmount;
	private float taxAmount;
	private float totalAmount;
	private int clientid;
	
	
	
	public Factura(int invoiceId, float untaxedAmount, float taxAmount, float totalAmount, int clientid) {
		super();
		this.invoiceId = invoiceId;
		this.untaxedAmount = untaxedAmount;
		this.taxAmount = taxAmount;
		this.totalAmount = totalAmount;
		this.clientid = clientid;
	}
	public int getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
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
	public float getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(float totalAmount) {
		this.totalAmount = totalAmount;
	}
	public int getClientid() {
		return clientid;
	}
	public void setClientid(int clientid) {
		this.clientid = clientid;
	}
	
	
	
}
