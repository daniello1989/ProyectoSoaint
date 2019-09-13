package com.soaint.AWS.verificadores;
import org.json.JSONObject;
import com.soaint.AWS.model.AddressType;
import com.soaint.AWS.model.Contacto;
import com.soaint.AWS.model.Emails;
import com.soaint.AWS.model.Name;
import com.soaint.AWS.service.AwsService;

public class VerifierRN {
	
	//EL MÉTODO BUENO QUE USO EN SAVEUSEROTHER
public boolean checkUserExist(String contacto) throws Exception {
		
		AwsService s= new AwsService();
		Contacto c = new Contacto();
		JSONObject object = new JSONObject(contacto);
		c.setEmails(new Emails(object.getString("emailaddress"), new AddressType()));
		
		System.out.println(s.getUser(object.getString("emailaddress")).toString());
		//System.out.println(object.getString("id"));
		if(s.getUser(object.getString("emailaddress")).getId()==null) {
			return false;
		}//if
		return true;
	}//checkUser()

	public boolean checkUser(Contacto contacto, String email) throws Exception {
		
		AwsService servicio= new AwsService();
	
		if(servicio.getUser(email).getEmails().getAddress()==contacto.getEmails().getAddress()) {
			return true;	
		}//if
		
		return false;
}//checkUser()

}
