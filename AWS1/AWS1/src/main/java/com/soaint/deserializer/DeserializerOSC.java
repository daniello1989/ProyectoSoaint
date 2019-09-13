package com.soaint.deserializer;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soaint.AWS.model.ContactoOSC;

public class DeserializerOSC {
	
public ContactoOSC deserializeContact(JSONObject jsonObject) throws Exception{
		
	ContactoOSC contactoOSC= new ContactoOSC();
    ObjectMapper obj = new ObjectMapper();
	JsonNode node = obj.readTree(jsonObject.toString());
	contactoOSC.setPartyNumber(node.get("PartyNumber").asText());
	contactoOSC.setFirstName(node.get("FirstName").asText());
	contactoOSC.setLastName(node.get("LastName").asText());
	contactoOSC.setEmailAddress(node.get("EmailAddress").asText());

		return contactoOSC;
	}
}
