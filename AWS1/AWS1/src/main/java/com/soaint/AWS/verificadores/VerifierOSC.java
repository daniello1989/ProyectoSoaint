package com.soaint.AWS.verificadores;

import org.json.JSONObject;

import com.soaint.AWS.model.ContactoOSC;
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

	
	public boolean checkUserSave(ContactoOSC contacto) throws Exception {
		
		AwsServiceOSC servicio= new AwsServiceOSC();
		
		if(servicio.getUser(contacto.getEmailAddress())[0].getEmailAddress()!=null) {
			return true;	
		}//if
		return false;
	}//checkUser()

}
