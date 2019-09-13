package com.soaint.serializers;
import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soaint.AWS.model.Lead;

public class SerializerLeadOSC {
	
	public String serializeLead(Lead lead) throws ClientProtocolException, IOException {
		
		ObjectMapper obj= new ObjectMapper();
		return obj.writeValueAsString(lead);
	}	
}
