package com.soaint.AWS.verificadores;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soaint.AWS.model.ContactoOSC;
import com.soaint.AWS.model.Lead;
import com.soaint.AWS.service.AwsServiceOSC;

public class VerifierOSC {
	
	public boolean checkUserExist(String contacto) throws Exception {
		
		AwsServiceOSC servicio= new AwsServiceOSC();
		
		ContactoOSC c = new ContactoOSC();
		JSONObject object = new JSONObject(contacto);
		c.setFirstName(object.getString("firstname"));
		c.setLastName(object.getString("lastname"));
		c.setEmailAddress(object.getString("emailaddress"));
		
		if(servicio.getUser(c.getEmailAddress())[0].getEmailAddress()!=null) {
			return true;	
		}//if
		return false;
	}//checkUser()
	
	public boolean checkLeadExists(String contacto) throws Exception {
		
		AwsServiceOSC servicio= new AwsServiceOSC();

		Lead lead= new Lead();
		ObjectMapper obj = new ObjectMapper();
		JsonNode node = obj.readTree(contacto.toString());
		
		ContactoOSC[] c= servicio.getUser(node.get("emailaddress").asText());
		ContactoOSC contact= c[0];
				
		if(servicio.getLead(contact.getPartyNumber())=="") {
			return false;
		}
		
		return true;
	}

	public boolean checkUserSave(ContactoOSC contacto) throws Exception {
		
		AwsServiceOSC servicio= new AwsServiceOSC();
		
		if(servicio.getUser(contacto.getEmailAddress())[0].getEmailAddress()!=null) {
			return true;	
		}//if
		return false;
	}//checkUser()
	
	public boolean checkUser(ContactoOSC contacto, String email) throws Exception {
		
		AwsServiceOSC servicio= new AwsServiceOSC();

		if(servicio.getUser(email)[0].getEmailAddress()==contacto.getEmailAddress()) {
			
			return true;	
		}//if
		return false;
		}//checkUser()

}
