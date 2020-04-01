package com.odoojava.api.testing;

import java.util.ArrayList;

public class Cliente {
	
	private String vat;
	private int idcliente;
	private String name;
	private String email; 
	
	
	public Cliente() {
		super();
	}
	public String getVat() {
		return vat;
	}
	public void setVat(String vat) {
		this.vat = vat;
	}
	public int getIdcliente() {
		return idcliente;
	}
	public void setIdcliente(int idcliente) {
		this.idcliente = idcliente;
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
	private ArrayList<Factura> facturas;
	public Cliente(String vat, int idcliente, String name, String email, ArrayList<Factura> facturas) {
		super();
		this.vat = vat;
		this.idcliente = idcliente;
		this.name = name;
		this.email = email;
		this.facturas = facturas;
	}
	
	
	

	
	

	
	
	

}
