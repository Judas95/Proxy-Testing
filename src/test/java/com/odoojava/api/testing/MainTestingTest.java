package com.odoojava.api.testing;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.apache.xmlrpc.XmlRpcException;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.odoojava.api.OdooCommand;

import static java.util.Arrays.asList;

public class MainTestingTest {

	@Test
	public void readtest() throws Exception {
		MainTesting a = new MainTesting();
		a.openERPSession.startSession();	
		//ObjectAdapter algoAdapter = a.openERPSession.getObjectAdapter("res.partner");
		Object a2 = a.openERPSession.executeCommand("account.move", "search_read", new String[]{});
		//OdooCommand a3 = new OdooCommand(a.openERPSession);
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(a2);  
		System.out.println(json);
	}
	
	@Test
	public void createTest() throws Exception {
		MainTesting a = new MainTesting();
		a.openERPSession.startSession();	
		HashMap a3 = new HashMap();				
		a3.put("currency_id", 1);
		a3.put("name", "hola");		
		a3.put("date", "2020-05-01");
		a3.put("journal_id", 1);
		a3.put("currency_id", 1);
		a3.put("state", "draft");
		a3.put("type", "out_invoice");
		a3.put("partner_id", 14);
		a3.put("invoice_date", "2020-06-02");		
		Object a2 = a.openERPSession.executeCommand("account.move", "create", new Object[]{a3});
	}
	
	
}
