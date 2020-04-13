package com.odoojava.api.testing;

public class InvoiceLine {

	private int invoiceLineId;
	private String description;
	private float quantity;
	private int productId;
	private float priceUnit;
	private int accountId;
	private int invoiceId;
	
	public InvoiceLine(int invoiceLineId, String description, float quantity, int productId, float priceUnit,
			int accountId, int invoiceId) {
		super();
		this.invoiceLineId = invoiceLineId;
		this.description = description;
		this.quantity = quantity;
		this.productId = productId;
		this.priceUnit = priceUnit;
		this.accountId = accountId;
		this.invoiceId = invoiceId;
	}
	
	public int getInvoiceLineId() {
		return invoiceLineId;
	}
	public void setInvoiceLineId(int invoiceLineId) {
		this.invoiceLineId = invoiceLineId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public float getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public float getPriceUnit() {
		return priceUnit;
	}
	public void setPriceUnit(float priceUnit) {
		this.priceUnit = priceUnit;
	}
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public int getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Id línea: " + invoiceLineId + "\n"+
				"Descripción: " + description + "\n" +
				"Id producto: " + productId + "\n" + 
				"Cantidad: " + quantity + "\n" + 
				"Precio unidad: " + priceUnit + "\n" +
				"Id cuenta: " + accountId + "\n" +
				"Id factura: " + invoiceId;
	}
	
	
	
	
}
