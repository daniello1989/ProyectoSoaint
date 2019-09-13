package com.soaint.AWS.verificadores;
import org.json.JSONObject;
import com.soaint.AWS.model.AddressType;
import com.soaint.AWS.model.Contacto;
import com.soaint.AWS.model.Emails;
import com.soaint.AWS.model.Name;
import com.soaint.AWS.service.AwsService;

public class VerifierRN {
	
public boolean checkUserExist(String contacto) throws Exception {
		
		AwsService s= new AwsService();
		Contacto c = new Contacto();
		JSONObject object = new JSONObject(contacto);
		c.setName(new Name(object.getString("firstname"), object.getString("lastname")));
		c.setEmails(new Emails(object.getString("emailaddress"), new AddressType()));
		
		String mail=s.getUser(c.getEmails().getAddress()).getEmails().getAddress();
		System.out.println(s.getUser(c.getEmails().getAddress()));
		if(s.getUser(c.getEmails().getAddress().toString()).getEmails().getAddress()!=null) {
			return true;
		}//if
		return false;
	}//checkUser()

}
