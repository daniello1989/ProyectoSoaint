package com.soaint.serializers;
import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soaint.AWS.model.ContactoEloqua;
import com.soaint.transformer.UriTransformer;

public class SerializerEloqua {
	
	public String serializeContact(String contacto) throws ClientProtocolException, IOException {
		ContactoEloqua contactoEloqua = new ContactoEloqua();
		JSONObject object = new JSONObject(contacto);
		contactoEloqua.setName(UriTransformer.JsonTransformerJSON(object.getString("firstname")));
		contactoEloqua.setEmailAddress(UriTransformer.JsonTransformerJSON(object.getString("emailaddress")));
		ObjectMapper obj = new ObjectMapper();
		return obj.writeValueAsString(contactoEloqua).toString();
   	}
}
