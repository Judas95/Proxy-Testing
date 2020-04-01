package com.odoojava.api.testing;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Arrays;
import java.util.List;
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
	
	static Session openERPSession = new Session("192.168.56.102", 8069, "ProyectoEmpresa", "zascazasca95@gmail.com", "1234");
	static ObjectAdapter partnerAd;
	static ObjectAdapter reseando;
	static Scanner scan = new Scanner(System.in);

	public static void main(String[] args) throws Exception {
		    // startSession logs into the server and keeps the userid of the logged in user
		    openERPSession.startSession();
		    
		    int options = 0;
		    do {
		    	System.out.println("1.Leer facturas , 2.Borrar factura ,3.Crear facturita linda ,4.factura4,5.Clientes");
		    	options = scan.nextInt();
		    	switch(options){
			    case 1 :
			    	readInvoice();
			    	break;
			    case 2 :
			    	deleteInvoice();
			    	break;
			    case 3 :
			    	//createInvoice();
			    	break;
			    case 4 :
		    		checkproducts();
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
    			System.out.println("No tiene"); 		
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
//        		for (int i = 0; i < objectIds.length; i++) {
//    				System.out.println(objectIds[i]);
//    			}	
        		
    			RowCollection invoices = partnerAd.readObject(objectIds, new String[]{"amount_untaxed","amount_tax","amount_total"});
            		invoicesArray = new ArrayList<Factura>();
            		for (Row row2 : invoices) {			
            			invoicesArray.add(new Factura(row2.getID(),
            										  Float.parseFloat(String.valueOf(row2.get("amount_untaxed"))),
            										  Float.parseFloat(String.valueOf(row2.get("amount_tax"))),
            										  Float.parseFloat(String.valueOf(row2.get("amount_total"))), 
            										  row.getID()));	
        			}
            		
            		client.setFacturas(invoicesArray);
            
            		for (Factura factura : invoicesArray) {
        				System.out.println(factura.getInvoiceId());
        				System.out.println(factura.getUntaxedAmount());
        				System.out.println(factura.getTaxAmount());
        				System.out.println(factura.getTotalAmount());
        				
        				System.out.println(factura.getClientid());
        			}
    		}
    		
    		

    		  	
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    		
    	
    }
	}

//  CONTROL SHIFT C 
//	private static void createInvoice() throws XmlRpcException, OdooApiException {
//		// CREAR INVOICE CRACK
//		partnerAd =  openERPSession.getObjectAdapter("account.invoice.line");
//		reseando = openERPSession.getObjectAdapter("res.partner");
//		int[] arrayint = new int[] {1,2,3,4,5};
//		
//		Row newPartner = partnerAd.getNewRow(new String[]{"name","price_unit","quantity","product_id","invoice_id","account_id","invoice_line_tax_ids"});
//		newPartner.put("name","Drogas duras");
//		newPartner.put("price_unit",200);
//		newPartner.put("quantity",30);
//		newPartner.put("product_id",14);
//		newPartner.put("invoice_id",13);
//		newPartner.put("account_id",480);
//		newPartner.put("invoice_line_tax_ids",arrayint);
//		partnerAd.createObject(newPartner);	
//	}

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
				//System.out.println(row.getFields());
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
		Object[] alguito = null;
		reseando = openERPSession.getObjectAdapter("res.partner");
		partnerAd =  openERPSession.getObjectAdapter("account.invoice");		
	    FilterCollection filters = new FilterCollection();
	    //filters.add("customer","=",true);    
	    RowCollection partners = partnerAd.searchAndReadObject(filters, new String[]{"partner_id","amount_total"});
		for (Row row : partners){		
	    	//System.out.println(row.getFields());
	        System.out.println("Row ID: " + row.getID());
	    	System.out.println("partnerid:" + row.get("partner_id"));
	    	System.out.println("Precio:" + row.get("amount_total"));    	
	    	alguito = new Object[] {row.get("partner_id")};	    	
	    	RowCollection nombre = reseando.readObject(alguito, new String[]{"display_name"});    	
	    	for(Row A : nombre) {
	    		System.out.println("nombre:" + A.get("display_name"));
	    	}
	    }
	} 
								////IMPORTANTE IMPUESTOS////
	private static void checkproducts() throws XmlRpcException, OdooApiException {
		// TODO Auto-generated method stub
		reseando = openERPSession.getObjectAdapter("res.partner");
		partnerAd =  openERPSession.getObjectAdapter("account.invoice.line");
		
		FilterCollection filters = new FilterCollection();
		filters.add("id","=",14);
		RowCollection partners = partnerAd.searchAndReadObject(filters, new String[]{"invoice_id","pricetotal","invoice_line_tax_ids"});

		// You could do some validation here to see if the customer was found
		Row AgrolaitRow = partners.get(0);
		AgrolaitRow.put("invoice_line_tax_ids", new Object[]{1});
		// Tell writeObject to only write changes ie the name isn't updated because it wasn't changed.
		boolean success = partnerAd.writeObject(AgrolaitRow, true);

		if (success)
		    System.out.println("Update was successful");
	}
	
	
	
	}


