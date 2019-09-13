package com.soaint.serializers;
import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soaint.AWS.model.ContactoOSC;
import com.soaint.transformer.UriTransformer;

public class SerializerOSC {

	public String serializeContact(String contacto) throws ClientProtocolException, IOException {
		
		ContactoOSC contactoOSC = new ContactoOSC();
		ObjectMapper obj = new ObjectMapper();
		JsonNode node = obj.readTree(contacto);
		contactoOSC.setFirstName(node.get(UriTransformer.JsonTransformerJSON("firstname")).asText());
		contactoOSC.setLastName(node.get(UriTransformer.JsonTransformerJSON("lastname")).asText());
		contactoOSC.setEmailAddress(node.get(UriTransformer.JsonTransformerJSON("emailaddress")).asText());
		
		return obj.writeValueAsString(contactoOSC);
   	}

	public ContactoOSC deserializeContact(String contacto) throws Exception{
		
		ContactoOSC contactoOSC = new ContactoOSC();
		ObjectMapper obj = new ObjectMapper();
		JsonNode node = obj.readTree(contacto);
		contactoOSC.setFirstName(node.get("firstname").asText());
		contactoOSC.setLastName(node.get("lastname").asText());
		contactoOSC.setEmailAddress(node.get("emailaddress").asText());

		return contactoOSC;
	}
}
