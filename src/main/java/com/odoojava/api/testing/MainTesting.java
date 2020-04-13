package com.odoojava.api.testing;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.apache.xmlrpc.XmlRpcException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.odoojava.api.FilterCollection;
import com.odoojava.api.ObjectAdapter;
import com.odoojava.api.OdooApiException;
import com.odoojava.api.Row;
import com.odoojava.api.RowCollection;
import com.odoojava.api.Session;

public class MainTesting {
	
	static Session openERPSession = new Session("192.168.1.54", 8069, "ProyectoEmpresa", "zascazasca95@gmail.com", "1234");
	static ObjectAdapter partnerAd;
	static ObjectAdapter reseando;
	static ObjectAdapter productObject;
	static Scanner scan = new Scanner(System.in);

	public static void main(String[] args) throws Exception {
		    // startSession logs into the server and keeps the userid of the logged in user
		    openERPSession.startSession();
		    
		    int options = 0;
		    do {
		    	System.out.println("1.Leer facturas , 2.Borrar factura ,3.Crear facturita ,4. Actualizar factura , 5.Clientes");
		    	options = scan.nextInt();
		    	switch(options){
			    case 1 :
			    	readInvoice();
			    	break;
			    case 2 :
			    	deleteInvoice();
			    	break;
			    case 3 :
			    	createInvoice();
			    	break;
			    case 4 :
			    	updateInvoice();
			    	break;
			    case 5 :
			    	checkClients();
			    	break;
			    
			    	
			    }    	
		    }while(options != 0); 
		}

private static void checkClients() throws XmlRpcException, OdooApiException, NoSuchFieldException, SecurityException, JsonProcessingException {
	partnerAd =  openERPSession.getObjectAdapter("account.invoice");
	reseando = openERPSession.getObjectAdapter("res.partner");
	Cliente client = null;
	ArrayList<Factura> invoicesArray = null;
	
	FilterCollection filters = new FilterCollection();
    filters.add("supplier","=", true);
    RowCollection partners = reseando.searchAndReadObject(filters, new String[]{"invoice_ids","name","email","vat"});
    
    ObjectMapper mapper = null;
    for (Row row : partners){
    		mapper = new ObjectMapper();
    		String json = mapper.writeValueAsString(row.get("invoice_ids"));   		    		    		
    		json = json.replace("[", "");
    		json = json.replace("]", "");   
    		if(json.contains("null")){
    				
    		}else {
    			String[] invoicesIdsArray= json.split(",");
    			
    			client = new Cliente();
        		client.setVat(String.valueOf(row.get("vat")));
           		client.setName(String.valueOf(row.get("name")));
        		client.setEmail(String.valueOf(row.get("email")));
        		client.setIdcliente(row.getID());
        		
        		System.out.println(client.getVat());
        		System.out.println(client.getName());
        		System.out.print(client.getEmail());
        		System.out.println(client.getIdcliente());
    			
    			Object[] objectIds = new Object[invoicesIdsArray.length];
    			
        		for (int i = 0; i < invoicesIdsArray.length; i++) {        					
        				objectIds[i] = Integer.parseInt(invoicesIdsArray[i]);     				      			
    			}        		
        		
    			RowCollection invoices = partnerAd.readObject(objectIds, new String[]{"amount_untaxed","amount_tax","amount_total","date_invoice","date_due"});
            		invoicesArray = new ArrayList<Factura>();
            		for (Row row2 : invoices) {			
            			invoicesArray.add(new Factura(row2.getID(),
            										  Float.parseFloat(String.valueOf(row2.get("amount_untaxed"))),
            										  Float.parseFloat(String.valueOf(row2.get("amount_tax"))),
            										  Float.parseFloat(String.valueOf(row2.get("amount_total"))), 
            										  row.getID(),
            										  String.valueOf(row2.get("date_invoice")),
            										  String.valueOf(row2.get("date_due"))
            										  ));	
        			}
            		
            		client.setFacturas(invoicesArray);
            
            		for (Factura factura : invoicesArray) {
        				System.out.println(factura.getInvoiceId());
        				System.out.println(factura.getUntaxedAmount());
        				System.out.println(factura.getTaxAmount());
        				System.out.println(factura.getTotalAmount());       			
        			}
    		}
    }
	}

//  CONTROL SHIFT C 
	private static void createInvoice() throws XmlRpcException, OdooApiException {
		// CREAR INVOICE CRACK
		Scanner scan2 = new Scanner(System.in);
		partnerAd =  openERPSession.getObjectAdapter("account.invoice");
		productObject =  openERPSession.getObjectAdapter("account.invoice.line");
		reseando = openERPSession.getObjectAdapter("res.partner");
	
		System.out.println("Introduzca el id del cliente");
		int id = scan.nextInt();
		
		System.out.println("Fecha de la factura");
		String date_invoice = scan2.nextLine();
		
		System.out.println("Fecha de vencimiento");
		String date_due = scan2.nextLine();
				
		Row newInvoice = partnerAd.getNewRow(new String[]{"account_id","partner_id","date_invoice","date_due"});
		newInvoice.put("account_id",480);
		newInvoice.put("partner_id",id);
		newInvoice.put("date_invoice",date_invoice);
		newInvoice.put("date_due",date_due);
		int a = partnerAd.createObject(newInvoice);	
		
	    int options = 0;
	    do {
	    	
	    	System.out.println("1.Añadir producto a factura. , 0.Terminar factura.");
	    	options = scan.nextInt();
	    	switch(options){
		    case 1 :
				
				System.out.println("Descripción");
				String description2 = scan2.nextLine();
				
				System.out.println("Id product");
				int product_id = scan2.nextInt();
				
				System.out.println("Precio por unidad");
				int price_unit = scan2.nextInt();
				
				System.out.println("Cantidad");
				int quantity = scan2.nextInt();
				
				
		    	Row newProduct = productObject.getNewRow(new String[] {"name","quantity","product_id","price_unit","account_id","invoice_id"});
				newProduct.put("name", description2);
				newProduct.put("quantity", quantity);
				newProduct.put("product_id", product_id);
				newProduct.put("price_unit", price_unit);
				newProduct.put("account_id", 480);
				newProduct.put("invoice_id", a);
				int idInvoiceLine = productObject.createObject(newProduct);							
				
				addTax(a,idInvoiceLine);
		    	break;	  
		    }    	
	    }while(options != 0); 
	    
	    
	}
	private static void addTax(int a, int idInvoiceLine) throws XmlRpcException, OdooApiException {
		reseando = openERPSession.getObjectAdapter("res.partner");
		partnerAd =  openERPSession.getObjectAdapter("account.invoice.line");
		
		FilterCollection filters = new FilterCollection();
		filters.add("id","=",idInvoiceLine);
		RowCollection partners = partnerAd.searchAndReadObject(filters, new String[]{"invoice_id","pricetotal","invoice_line_tax_ids"});

		// You could do some validation here to see if the customer was found
		Row AgrolaitRow = partners.get(0);
		AgrolaitRow.put("invoice_line_tax_ids", new Object[]{1});
		// Tell writeObject to only write changes ie the name isn't updated because it wasn't changed.
		boolean success = partnerAd.writeObject(AgrolaitRow, true);

		if (success) {
			System.out.println("Update was successful");
			openERPSession.executeCommand("account.invoice", "compute_taxes", new Object[]{a});
		}
		   
	}
		
	


	private static void deleteInvoice() throws XmlRpcException, OdooApiException {
		// DELETE INVOICE 
		Object[] alguito = null;
		partnerAd =  openERPSession.getObjectAdapter("account.invoice");
		reseando = openERPSession.getObjectAdapter("res.partner");
		System.out.println("Escribe el ID del que desea borrar");
		
		int id = scan.nextInt();
		FilterCollection filters = new FilterCollection();
		filters.add("id","=",id);
		RowCollection partners = partnerAd.searchAndReadObject(filters, new String[]{"partner_id","amount_total"});
			for (Row row : partners){				
				
				System.out.println("Id: " + row.getID());
				System.out.println("partnerid:" + row.get("partner_id"));
		    	System.out.println("Precio:" + row.get("amount_total"));		    	
		    	alguito = new Object[] {row.get("partner_id")};	    	
		    	RowCollection nombre = reseando.readObject(alguito, new String[]{"display_name"});   	
		    	for(Row A : nombre) {
		    		System.out.println("nombre:" + A.get("display_name"));
		    	}
	    }
		partnerAd.unlinkObject(partners);	
	}

	private static void readInvoice() throws XmlRpcException, OdooApiException {
		// READ INVOICE		
		HashMap<Factura,ArrayList<InvoiceLine>> map=new HashMap<Factura,ArrayList<InvoiceLine>>();   		
		ArrayList<InvoiceLine> invoicesLines;
		reseando = openERPSession.getObjectAdapter("account.invoice.line");
		partnerAd =  openERPSession.getObjectAdapter("account.invoice");		
		FilterCollection filtersInvoices = new FilterCollection();
		FilterCollection filtersLine;
		RowCollection lines;
	    RowCollection invoices = partnerAd.searchAndReadObject(filtersInvoices, new String[]{"partner_id",
				"vendor_display_name",
				"amount_total","amount_tax",
				"amount_untaxed","date_invoice",
				"date_due"});	   
		
		for (Row row : invoices){			
			System.out.println(row.getID());
			invoicesLines = new ArrayList<InvoiceLine>();	
			filtersLine = new FilterCollection();
			filtersLine = new FilterCollection();
			filtersLine.add("invoice_id","=",row.getID());    			
			lines = reseando.searchAndReadObject(filtersLine, new String[] {"name",
					  "quantity",
					  "product_id",
					  "price_unit",
					  "account_id",
					  "invoice_id"});			
			for (Row row2 : lines) {		
				System.out.println(row2.getID());
				invoicesLines.add(new InvoiceLine(row2.getID(), 
												  row2.get("name").toString(), 
												  Float.parseFloat(row2.get("quantity").toString()), 
												  Integer.parseInt(row2.get("product_id").toString()), 
												  Float.parseFloat(row2.get("price_unit").toString()),
												  Integer.parseInt(row2.get("account_id").toString()), 
												  row.getID()));				
			}						
			map.put(new Factura(row.getID(), 
					Float.parseFloat(row.get("amount_untaxed").toString()), 
					Float.parseFloat(row.get("amount_tax").toString()), 
					Float.parseFloat(row.get("amount_total").toString()), 
					Integer.parseInt(row.get("partner_id").toString()),
					row.get("date_invoice").toString(), 
					row.get("date_due").toString()), invoicesLines);   
	    }
		
		for (Entry entry : map.entrySet()) {
			System.out.println(entry.getKey().toString());
			System.out.println("Productos: ");	
			System.out.println(entry.getValue().toString());			
			System.out.println("-------------------------");
		}

	} 

	
	public static void updateInvoice() throws OdooApiException, XmlRpcException {
		// DELETE INVOICE 
		Scanner scan2 = new Scanner(System.in);
		partnerAd = openERPSession.getObjectAdapter("account.invoice");	
		System.out.println("Introduzca el id de la factura");
		int invoiceId = scan.nextInt();
		
		
		FilterCollection filters = new FilterCollection();		
		filters.add("id","=",invoiceId);
		RowCollection partners = partnerAd.searchAndReadObject(filters, new String[]{"partner_id",
																					"vendor_display_name",
																					"amount_total","amount_tax",
																					"amount_untaxed","date_invoice",
																					"date_due"});
		Row AgrolaitRow = partners.get(0);
		Factura invoice = new Factura(AgrolaitRow.getID(), 
				Float.parseFloat(AgrolaitRow.get("amount_untaxed").toString()), 
				Float.parseFloat(AgrolaitRow.get("amount_tax").toString()), 
				Float.parseFloat(AgrolaitRow.get("amount_total").toString()), 
				Integer.parseInt(AgrolaitRow.get("partner_id").toString()),
				AgrolaitRow.get("date_invoice").toString(), 
				AgrolaitRow.get("date_due").toString());
		
		System.out.println("Cliente: "+ AgrolaitRow.get("vendor_display_name"));
		System.out.println("Sin impuestos: " + invoice.getUntaxedAmount());
		System.out.println("Total del impuesto: " + invoice.getTaxAmount());
		System.out.println("Total: " + invoice.getTotalAmount());
		System.out.println("Fecha de la factura: "+ invoice.getDate_invoice());
		System.out.println("Fecha de vencimiento: "+ invoice.getDate_due());
		
		
		System.out.println("Introduzca el nuevo id del cliente");
		int newId = scan.nextInt();
		System.out.println("Introduzca la fecha de vencimiento ");
		String date = scan2.nextLine();
		
		AgrolaitRow.put("partner_id", newId);
		AgrolaitRow.put("date_due", date);
		
		// Tell writeObject to only write changes ie the name isn't updated because it wasn't changed.
		boolean success = partnerAd.writeObject(AgrolaitRow, true);
		
		if (success)
		    System.out.println("Update was successful");
	}
	
	
	}


