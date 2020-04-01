package com.odoojava.api.testing;

public class Cliente {
	
	private String name;
	private String email;
	private Factura[] facturas;
	
	
	public Cliente(String name, String email, Factura[] facturas) {
		super();
		this.name = name;
		this.email = email;
		this.facturas = facturas;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public Factura[] getFacturas() {
		return facturas;
	}


	public void setFacturas(Factura[] facturas) {
		this.facturas = facturas;
	}
	
	

}
