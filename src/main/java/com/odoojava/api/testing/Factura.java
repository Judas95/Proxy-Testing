package com.odoojava.api.testing;

public class Factura {

	private int invoiceId;
	private float untaxedAmount;
	private float taxAmount;
	private float totalAmount;
	private int clientid;
	private String date_invoice;
	private String date_due;
	
	
	public Factura(int invoiceId, float untaxedAmount, float taxAmount, float totalAmount, int clientid,
			String date_invoice, String date_due) {
		super();
		this.invoiceId = invoiceId;
		this.untaxedAmount = untaxedAmount;
		this.taxAmount = taxAmount;
		this.totalAmount = totalAmount;
		this.clientid = clientid;
		this.date_invoice = date_invoice;
		this.date_due = date_due;
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
	public String getDate_invoice() {
		return date_invoice;
	}
	public void setDate_invoice(String date_invoice) {
		this.date_invoice = date_invoice;
	}
	public String getDate_due() {
		return date_due;
	}
	public void setDate_due(String date_due) {
		this.date_due = date_due;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Id factura: "+ invoiceId + "\n" + 
			   "Total sin impuestos: " + untaxedAmount + "\n" + 
			   "Total impuestos: " + taxAmount + "\n" + 
			   "Total: " + totalAmount + "\n" + 
			   "Id cliente: " + clientid + "\n" +
			   "Fecha de la factura: " + date_invoice + "\n" +
			   "Fecha del vencimiento: " +date_due;
		
	}
	
	
	
}
