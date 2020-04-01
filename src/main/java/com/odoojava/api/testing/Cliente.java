package com.odoojava.api.testing;

import java.util.ArrayList;

public class Cliente {
	
	private String vat;
	private String name;
	private String email;
	private ArrayList<Factura> facturas;
	
	
	public Cliente(String vat, String name, String email, ArrayList<Factura> facturas) {
		super();
		this.name = name;
		this.email = email;
		this.facturas = facturas;
		this.vat = vat;
	}
	
	

	public String getVat() {
		return vat;
	}

	public void setVat(String vat) {
		this.vat = vat;
	}



	public Cliente() {
		super();
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


	public ArrayList<Factura> getFacturas() {
		return facturas;
	}


	public void setFacturas(ArrayList<Factura> facturas) {
		this.facturas = facturas;
	}



	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.name;
	}

	
	

	
	
	

}
