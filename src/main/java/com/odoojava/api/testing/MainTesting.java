package com.odoojava.api.testing;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.Map.Entry;
import java.util.Scanner;

import org.apache.xmlrpc.XmlRpcException;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.odoojava.api.FilterCollection;
import com.odoojava.api.ObjectAdapter;
import com.odoojava.api.OdooApiException;
import com.odoojava.api.Row;
import com.odoojava.api.RowCollection;
import com.odoojava.api.Session;


public class MainTesting {
	
	static Session openERPSession = new Session("192.168.1.57", 8069, "Testing", "carlosha98@gmail.com", "1234");
	static Scanner scan = new Scanner(System.in);
	static Scanner scan2 = new Scanner(System.in);
	public static void main(String[] args) throws Exception {
		    // startSession logs into the server and keeps the userid of the logged in user
		    openERPSession.startSession();		    
		    int options = 0;
		    showMainMenu();
		    mainOptionSwitch(options);		    
		}

	private static void mainOptionSwitch(int options) throws XmlRpcException, OdooApiException, NoSuchFieldException, SecurityException, JsonProcessingException {
		do {	    	
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
	ObjectAdapter invoiceObjectAdapter =  openERPSession.getObjectAdapter("account.invoice");
	ObjectAdapter partnerObjectAdapter = openERPSession.getObjectAdapter("res.partner");
	Cliente client = null;
	ArrayList<Factura> invoicesArray = null;
	
	FilterCollection filters = new FilterCollection();
    filters.add("supplier","=", true);
    RowCollection partners = partnerObjectAdapter.searchAndReadObject(filters, new String[]{"invoice_ids","name","email","vat"});
    
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
        		
    			RowCollection invoices = invoiceObjectAdapter.readObject(objectIds, new String[]{"amount_untaxed","amount_tax","amount_total","date_invoice","date_due"});
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


	private static void createInvoice() throws XmlRpcException, OdooApiException {		
		ObjectAdapter invoiceObjectAdapter =  openERPSession.getObjectAdapter("account.invoice");
		ObjectAdapter invoiceLineObjectAdapter =  openERPSession.getObjectAdapter("account.invoice.line");
	
		System.out.println("Introduzca el id del cliente");
		int id = scan.nextInt();
		
		System.out.println("Fecha de la factura");
		String date_invoice = scan2.nextLine();
		
		System.out.println("Fecha de vencimiento");
		String date_due = scan2.nextLine();						
		
		int invoiceId = invoiceObjectAdapter.createObject(prepareNewInvoiceRow(id, date_invoice,date_due, invoiceObjectAdapter));	
		
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
				int idInvoiceLine = invoiceLineObjectAdapter.createObject(prepareNewInvoiceLine(description2,product_id, price_unit,quantity, invoiceId, invoiceLineObjectAdapter));							
				
				addTax(invoiceId,idInvoiceLine);
		    	break;	  
		    }    	
	    }while(options != 0); 
	    
	    
	}
	private static Row prepareNewInvoiceLine(String description2, int product_id, int price_unit, int quantity, int invoiceId, ObjectAdapter invoiceLineObjectAdapter) throws XmlRpcException, OdooApiException {
		Row newProduct = invoiceLineObjectAdapter.getNewRow(new String[] {"name","quantity","product_id","price_unit","account_id","invoice_id"});
		newProduct.put("name", description2);
		newProduct.put("quantity", quantity);
		newProduct.put("product_id", product_id);
		newProduct.put("price_unit", price_unit);
		newProduct.put("account_id", 480);
		newProduct.put("invoice_id", invoiceId);
		return newProduct;
	}

	public static Row prepareNewInvoiceRow(int id, String date_invoice, String date_due, ObjectAdapter invoiceObjectAdapter) throws XmlRpcException, OdooApiException {
		Row newInvoice = invoiceObjectAdapter.getNewRow(new String[]{"account_id","partner_id","date_invoice","date_due"});
		newInvoice.put("account_id",480);
		newInvoice.put("partner_id",id);
		newInvoice.put("date_invoice",date_invoice);
		newInvoice.put("date_due",date_due);
		return newInvoice;
	}

	private static void addTax(int a, int idInvoiceLine) throws XmlRpcException, OdooApiException {	
		ObjectAdapter invoiceLineObjectAdapter =  openERPSession.getObjectAdapter("account.invoice.line");		
		FilterCollection filters = new FilterCollection();
		filters.add("id","=",idInvoiceLine);
		RowCollection partners = invoiceLineObjectAdapter.searchAndReadObject(filters, new String[]{"invoice_id","pricetotal","invoice_line_tax_ids"});

		Row AgrolaitRow = partners.get(0);
		AgrolaitRow.put("invoice_line_tax_ids", new Object[]{1});

		boolean success = invoiceLineObjectAdapter.writeObject(AgrolaitRow, true);
		if (success) {
			System.out.println("Update was successful");
			openERPSession.executeCommand("account.invoice", "compute_taxes", new Object[]{a});
		}		   
	}
		
	


	private static void deleteInvoice() throws XmlRpcException, OdooApiException {
//		Object[] alguito = null;
		ObjectAdapter invoiceObjectAdapter =  openERPSession.getObjectAdapter("account.invoice");
		//ObjectAdapter partnerObjectAdapter = openERPSession.getObjectAdapter("res.partner");
		System.out.println("Escribe el ID del que desea borrar");		
		int id = scan.nextInt();
		FilterCollection filters = new FilterCollection();
		filters.add("id","=",id);
		RowCollection partners = invoiceObjectAdapter.searchAndReadObject(filters, new String[]{"partner_id","amount_total","vendor_display_name"});
			for (Row row : partners){						
				System.out.println("Id: " + row.getID());
				System.out.println("Partnerid:" + row.get("partner_id"));
		    	System.out.println("Precio:" + row.get("amount_total"));	
		    	System.out.println("Nombre" + row.get("vendor_display_name"));
//		    	alguito = new Object[] {row.get("partner_id")};	    	
//		    	RowCollection nombre = partnerObjectAdapter.readObject(alguito, new String[]{"display_name"});   	
//		    	for(Row A : nombre) {
//		    		System.out.println("nombre:" + A.get("display_name"));
//		    	}
	    }
			invoiceObjectAdapter.unlinkObject(partners);	
	}

	private static void readInvoice() throws XmlRpcException, OdooApiException {		 			
		ObjectAdapter invoiceLineObjectAdapter = openERPSession.getObjectAdapter("account.invoice.line");
		ObjectAdapter invoiceObjectAdapter =  openERPSession.getObjectAdapter("account.invoice");		
		FilterCollection filtersInvoices = new FilterCollection();		
	    RowCollection invoices = invoiceObjectAdapter.searchAndReadObject(filtersInvoices, new String[]{"partner_id",
				"vendor_display_name",
				"amount_total","amount_tax",
				"amount_untaxed","date_invoice",
				"date_due"});	   		
	    readInvoiceAndInvoiceLineRowCollection(invoices,invoiceLineObjectAdapter);		
	} 

	private static void readInvoiceAndInvoiceLineRowCollection(RowCollection invoices, ObjectAdapter invoiceLineObjectAdapter) throws XmlRpcException, OdooApiException {
		HashMap<Factura,ArrayList<InvoiceLine>> map=new HashMap<Factura,ArrayList<InvoiceLine>>();  
		RowCollection lines = null;
		FilterCollection filtersLine = null;
		ArrayList<InvoiceLine> invoicesLines = null;
		for (Row row : invoices){			
			System.out.println(row.getID());
			invoicesLines = new ArrayList<InvoiceLine>();	
			filtersLine = new FilterCollection();
			filtersLine.add("invoice_id","=",row.getID());    			
			lines = invoiceLineObjectAdapter.searchAndReadObject(filtersLine, new String[] {"name",
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
			fillInvoiceMap(invoicesLines, row, map);
			
	    }		
		readInvoiceMap(map);		
		
	}

	public static void fillInvoiceMap(ArrayList<InvoiceLine> invoicesLines, Row row, HashMap<Factura, ArrayList<InvoiceLine>> map){
		map.put(new Factura(row.getID(), 
				Float.parseFloat(row.get("amount_untaxed").toString()), 
				Float.parseFloat(row.get("amount_tax").toString()), 
				Float.parseFloat(row.get("amount_total").toString()), 
				Integer.parseInt(row.get("partner_id").toString()),
				row.get("date_invoice").toString(), 
				row.get("date_due").toString()), invoicesLines);   
	}
	private static void readInvoiceMap(HashMap<Factura, ArrayList<InvoiceLine>> map) {
		for (Entry entry : map.entrySet()) {
			System.out.println(entry.getKey().toString());
			System.out.println("Productos: ");	
			System.out.println(entry.getValue().toString());			
			System.out.println("-------------------------");
		}		
	}

	public static void updateInvoice() throws OdooApiException, XmlRpcException {
		ArrayList<InvoiceLine> arrayInvoiceLine = new ArrayList<InvoiceLine>();
		ObjectAdapter invoiceLineObjectAdapter = openERPSession.getObjectAdapter("account.invoice.line");	
		ObjectAdapter invoiceObjectAdapter = openERPSession.getObjectAdapter("account.invoice");	
		System.out.println("Introduzca el id de la factura");
		int invoiceId = scan.nextInt();
				
		FilterCollection filters = new FilterCollection();		
		filters.add("id","=",invoiceId);
		RowCollection partners = invoiceObjectAdapter.searchAndReadObject(filters, new String[]{"partner_id",
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
		
		FilterCollection lineFilters = new FilterCollection();
		lineFilters.add("invoice_id","=",invoiceId);
		
		RowCollection invoiceLines = invoiceLineObjectAdapter.searchAndReadObject(lineFilters, new String[] {"name",
				  "quantity",
				  "product_id",
				  "price_unit",
				  "account_id",
				  "invoice_id"});
		
		System.out.println("Cliente: "+ AgrolaitRow.get("vendor_display_name"));
		System.out.println("Sin impuestos: " + invoice.getUntaxedAmount());
		System.out.println("Total del impuesto: " + invoice.getTaxAmount());
		System.out.println("Total: " + invoice.getTotalAmount());
		System.out.println("Fecha de la factura: "+ invoice.getDate_invoice());
		System.out.println("Fecha de vencimiento: "+ invoice.getDate_due());
		
		int index = 0;
		System.out.println("    ");
		System.out.println("Productos:");
		for (Row row : invoiceLines) {
			arrayInvoiceLine.add(new InvoiceLine(row.getID(), 
												row.get("name").toString(), 
												Float.parseFloat(row.get("quantity").toString()), 
												Integer.parseInt(row.get("product_id").toString()), 
												Float.parseFloat(row.get("price_unit").toString()), 
												Integer.parseInt(row.get("account_id").toString()), 
												invoiceId));
			System.out.println(index + " :"  + arrayInvoiceLine.get(index).toString());
			System.out.println("     ");
			index++;
		}
		
		int options = 0;
		do {
			System.out.println("<------------>");
			System.out.println("1: Editar cliente.");
			System.out.println("2: Editar fecha de vencimiento.");
			System.out.println("3: Editar productos.");
			System.out.println("0: Guardar cambios.");
			System.out.println("<------------>");
			options = scan.nextInt();
			switch (options) {
			case 1:
				System.out.println("Id cliente actual: " + invoice.getClientid());
				System.out.println("Introduzca el nuevo id del cliente");
				int newId = scan.nextInt();
				AgrolaitRow.put("partner_id", newId);
				break;

			case 2:
				System.out.println("Fecha de vencimiento actual: " + invoice.getDate_due());
				System.out.println("Introduzca la fecha de vencimiento ");
				String date = scan2.nextLine();
				AgrolaitRow.put("date_due", date);
				break;
				
			case 3:
				int options2 = 0;
				Row AgrolaitRow2 = null;
				do {
					System.out.println("<------------>");
					System.out.println("1: Añadir nuevo producto.");
					System.out.println("2: Editar productos existentes.");
					System.out.println("0: Guardar cambios.");
					System.out.println("<------------>");
					options2 = scan.nextInt();
					switch (options2) {
					case 1:
						System.out.println("Descripción");
						String description2 = scan2.nextLine();
						
						System.out.println("Id product");
						int product_id = scan.nextInt();
						
						System.out.println("Precio por unidad");
						int price_unit = scan.nextInt();
						
						System.out.println("Cantidad");
						int quantity = scan.nextInt();
												
				    	Row newProduct = invoiceLineObjectAdapter.getNewRow(new String[] {"name","quantity","product_id","price_unit","account_id","invoice_id"});
						newProduct.put("name", description2);
						newProduct.put("quantity", quantity);
						newProduct.put("product_id", product_id);
						newProduct.put("price_unit", price_unit);
						newProduct.put("account_id", 480);
						newProduct.put("invoice_id", invoice.getInvoiceId());
						int idInvoiceLine = invoiceLineObjectAdapter.createObject(newProduct);				
						addTax(invoice.getInvoiceId(),idInvoiceLine);
						break;
					case 2:
						System.out.println("Introduzca el número de línea");
						int invoiceLineNumber = scan.nextInt();
						AgrolaitRow2 = invoiceLines.get(invoiceLineNumber);
						
						System.out.println("Descripción actual: " + AgrolaitRow2.get("name"));
						System.out.println("Nueva descripción.");
						String nameUpdate = scan2.nextLine();
						
						System.out.println("Cantidad actual: " + AgrolaitRow2.get("quantity"));
						System.out.println("Nueva cantidad.");
						float quantityUpdate = scan.nextFloat();
						
						System.out.println("Id producto actual: " + AgrolaitRow2.get("product_id"));
						System.out.println("Nuevo id producto.");
						int productIdUpdate = scan.nextInt();
						
						System.out.println("Precio por unidad actual: " +  AgrolaitRow2.get("price_unit"));
						System.out.println("Nuevo precio por unidad.");
						float priceUnitUpdate = scan.nextFloat();

						AgrolaitRow2.put("name", nameUpdate);
						AgrolaitRow2.put("product_id", productIdUpdate);
						AgrolaitRow2.put("quantity", quantityUpdate);
						AgrolaitRow2.put("price_unit", priceUnitUpdate);
											
						break;
					}
				}while(options2!=0);
				if(AgrolaitRow2 != null) {
					boolean success = invoiceLineObjectAdapter.writeObject(AgrolaitRow2, true);
					if (success)
					    System.out.println("Update was successful");
				}				
			}
		}while(options!=0);		
		boolean success = invoiceObjectAdapter.writeObject(AgrolaitRow, true);
		
		if (success)
		    System.out.println("Update was successful");
	}
	
	
	public static void showMainMenu() {
		System.out.println("1.Leer facturas.");
    	System.out.println("2.Borrar factura.");
    	System.out.println("3.Crear facturita.");
    	System.out.println("4.Actualizar factura.");
    	System.out.println("5.Clientes.");
	}
	
	}


