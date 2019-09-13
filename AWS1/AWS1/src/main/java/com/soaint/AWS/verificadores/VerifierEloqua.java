package com.soaint.AWS.verificadores;
import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;
import com.soaint.AWS.model.ContactoEloqua;
import com.soaint.AWS.service.AwsServiceEloqua;

public class VerifierEloqua {

public boolean checkUserExist(String contacto) throws Exception {
		
		AwsServiceEloqua servicio= new AwsServiceEloqua();
		
		ContactoEloqua c = new ContactoEloqua();
		JSONObject object = new JSONObject(contacto);
		c.setName(object.getString("firstname"));
		c.setEmailAddress(object.getString("emailaddress"));
		
		//String email=UriTransformer.JsonTransformerURIEmail(servicio.getUser(c.getEmailAddress()).getEmailAddress()).toString();
		
		if(servicio.getUser(c.getEmailAddress()).getEmailAddress().toString()!=null) {
			return true;	
		}//if
		return false;
	}//checkUser()
	
	public boolean checkUserSave(ContactoEloqua contacto) throws ClientProtocolException, IOException {
		
		AwsServiceEloqua servicio= new AwsServiceEloqua();
		
		if(servicio.getUser(contacto.getEmailAddress()).getEmailAddress()!=null) {
			return true;	
		}//if
		return false;
	}//checkUser()
	
}
