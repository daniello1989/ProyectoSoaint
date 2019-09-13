package com.soaint.serializers;
import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soaint.AWS.model.AddressType;
import com.soaint.AWS.model.Contacto;
import com.soaint.AWS.model.Emails;
import com.soaint.AWS.model.Name;
import com.soaint.transformer.UriTransformer;

public class SerializerRN {
	
	
	public String serializeContact(String contacto) throws ClientProtocolException, IOException {
		Contacto contactoRN = new Contacto();
		JSONObject object = new JSONObject(contacto);
		contactoRN.setName(new Name(UriTransformer.JsonTransformerJSON(object.getString("firstname")), 
				UriTransformer.JsonTransformerJSON(object.getString("lastname"))));
		contactoRN.setEmails(new Emails(UriTransformer.JsonTransformerJSON(object.getString("emailaddress")),
				new AddressType()));
		ObjectMapper obj = new ObjectMapper();
		return obj.writeValueAsString(contactoRN).toString();
   	}	
}
