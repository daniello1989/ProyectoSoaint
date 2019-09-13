package com.soaint.deserializer;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soaint.AWS.model.Lead;

public class DeserializerLeadOSC {
	
	public String deserializateLead(JSONObject objeto) throws Exception{
	    
		Lead lead= new Lead();
		ObjectMapper obj = new ObjectMapper();
		JsonNode node = obj.readTree(objeto.toString());
		lead.setLeadId(node.get("LeadId").asText());
		String id= lead.getLeadId();
		System.out.println("ID: " + id);
		return id;
	}
}
