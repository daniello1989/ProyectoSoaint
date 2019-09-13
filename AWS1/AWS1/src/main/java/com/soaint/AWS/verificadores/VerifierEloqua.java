package com.soaint.AWS.verificadores;
import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;
import com.soaint.AWS.model.ContactoEloqua;
import com.soaint.AWS.service.AwsServiceEloqua;

public class VerifierEloqua {

	//EL QUE USO PARA EL MÉTODO GUARDAR
public boolean checkUserExist(String contacto) throws Exception {
		
		AwsServiceEloqua servicio= new AwsServiceEloqua();
		
		ContactoEloqua c = new ContactoEloqua();
		JSONObject object = new JSONObject(contacto);
		String mail=object.getString("emailaddress");
		
		
		if(servicio.getUser(object.getString("emailaddress")).getName()!=null) {
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
	
	public boolean checkUser(ContactoEloqua contacto, String email) throws ClientProtocolException, IOException {
		
		AwsServiceEloqua servicio= new AwsServiceEloqua();
		
		if(servicio.getUser(email).getEmailAddress()==contacto.getEmailAddress()) {
			return true;	
		}//if
			return false;
	}//checkUser()
}
