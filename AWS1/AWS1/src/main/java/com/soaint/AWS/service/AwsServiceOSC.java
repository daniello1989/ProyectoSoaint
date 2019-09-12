package com.soaint.AWS.service;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Properties;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.soaint.AWS.model.AddressType;
import com.soaint.AWS.model.Contacto;
import com.soaint.AWS.model.ContactoEloqua;
import com.soaint.AWS.model.ContactoOSC;
import com.soaint.AWS.model.Emails;
import com.soaint.AWS.model.Lead;
import com.soaint.AWS.model.Name;
import com.soaint.encoder.Coder;
import com.soaint.repository.IDao;
import com.soaint.security.PropertiesRead;
import com.soaint.security.PropertiesReader;
import com.soaint.transformer.UriTransformer;

@Service
public class AwsServiceOSC implements IDao{
	
	private UriTransformer transformer;
	private static PropertiesReader p;
	private Coder c;
	
	public AwsServiceOSC() throws FileNotFoundException, IOException {
		
		p=new PropertiesReader();
	}//constructor

	@Override
	public ContactoOSC[] getUser(String email) throws Exception {
		String emailTransformed= UriTransformer.JsonTransformerURIEmail(email);
		
		String url=p.getURLOSC()+emailTransformed;

		HttpClient client= HttpClientBuilder.create().build();
		HttpGet request= new HttpGet(url);
		
		setHeader(request);
		
		HttpResponse response= client.execute(request);
		
		String resultado= EntityUtils.toString(response.getEntity());
					
		if(response.getStatusLine().getStatusCode()==200) {
	    JSONObject jsonObject= new JSONObject(resultado);
	    JSONArray lista=jsonObject.getJSONArray("items");
	    
	    if(lista.length()>=1) {    
		ContactoOSC contactoOSC= new ContactoOSC();
	    	
	    jsonObject=lista.getJSONObject(0);

	    //contactoOSC=deserializeContact(jsonObject.toString());
		
	    ObjectMapper obj = new ObjectMapper();
		JsonNode node = obj.readTree(jsonObject.toString());
		contactoOSC.setPartyNumber(node.get("PartyNumber").asText());
		contactoOSC.setFirstName(node.get("FirstName").asText());
		contactoOSC.setLastName(node.get("LastName").asText());
		contactoOSC.setEmailAddress(node.get("EmailAddress").asText());

		ContactoOSC[] contacto= {contactoOSC};
	    System.out.println(contacto[0].toString());
		
	    return contacto;
	    } else{
	    	ContactoOSC c= new ContactoOSC();
		    ContactoOSC[] contacto= {c};
		    return contacto;
	    }
		}
    	ContactoOSC c= new ContactoOSC();
	    ContactoOSC[] contacto= {c};
	    return contacto;

			 }//getUser()

	@Override
	public void deleteUser(String PartyNumber) throws Exception {
		
		deleteLead(PartyNumber);
		String url=p.getURLDeleteOSC()+PartyNumber;
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpDelete request= new HttpDelete(url);
		setHeader(client, request);
	}//deleteUser()
	
	public void deleteLead(String PartyNumber) throws Exception {
		
		String id= getLead(PartyNumber);
		String url= p.deleteLead()+id;

		HttpClient client = HttpClientBuilder.create().build();
		HttpDelete request= new HttpDelete(url);
		setHeader(client, request);
		
	}//deleteLead()
	
	private String getLead(String PartyNumber) throws Exception {
		
		System.out.println("PARTY NUMBER: "+PartyNumber);
		String url="https://adc4-zgth-fa-ext.oracledemos.com/crmRestApi/resources/latest/leads?q=ContactPartyNumber="
		+PartyNumber;
		
		HttpClient client= HttpClientBuilder.create().build();
		HttpGet request= new HttpGet(url);
		
		setHeader(request);
		
		HttpResponse response= client.execute(request);
		String resultado= EntityUtils.toString(response.getEntity());
		
		System.out.println("************RESULSTADO*****************:"+resultado);
	   
	    JSONObject jsonObject= new JSONObject(resultado);
	    JSONArray lista=jsonObject.getJSONArray("items");

	    System.out.println(jsonObject);
	   
	    return deserializateLead(lista.getJSONObject(0));
	   
		}

	@Override
	public void saveUser(String email, String nombre) throws Exception {
	
		String url=p.saveUserOSC();
		
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request= new HttpPost(url);
        setHeader(client, request);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        ContactoOSC contacto= new ContactoOSC(email, nombre);
        
        if(checkUser(contacto, email)!=true){
        	String ejemplo= gson.toJson(contacto);

            StringEntity entity = new StringEntity(ejemplo,
                    ContentType.APPLICATION_JSON);
            
            System.out.println(ejemplo);

            request.setEntity(entity);
            HttpResponse response = client.execute(request);

            System.out.println("Response Code : "
                    + response.getStatusLine().getStatusCode());
        }    
	}//saveUser()
	
	@Override
	public void saveUserOther(String contacto) throws UnsupportedCharsetException, Exception {

		String contacTransformed=UriTransformer.JsonTransformerJSON(contacto);
		
		if(checkUserExist(contacto)==false) {
		String url=p.saveUserOSC();
		
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request= new HttpPost(url);

        System.out.println(contacto.toString());
        
        System.out.println("JSONSEND: " + serializeContact(contacto));
            StringEntity entity = new StringEntity(serializeContact(contacTransformed),
                    ContentType.APPLICATION_JSON);

           
        request.setEntity(entity);
        setHeader(client, request);
       
		ContactoOSC contactoOSC = new ContactoOSC();
		ObjectMapper obj = new ObjectMapper();
		JsonNode node = obj.readTree(contacto);
		contactoOSC.setEmailAddress(node.get(UriTransformer.JsonTransformerJSON("emailaddress")).asText());

        addLead(contactoOSC.getEmailAddress());
		}
	}
	
	public void setHeader(HttpClient client, HttpDelete delete) throws ClientProtocolException, IOException{
				
		delete.setHeader(HttpHeaders.AUTHORIZATION, 
				p.getBasic()+" "+c.codificador(p.getUserPswdOSC()));
		HttpResponse response= client.execute(delete);
		
		System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());

		System.out.println("Cliente eliminado");
	}//setHeader()
	
	public void setHeader(HttpGet get) throws ClientProtocolException, IOException {
				
		System.out.println(c.codificador(p.getBasic())
				+c.codificador(p.getUserPswdOSC()));
		
		get.setHeader(HttpHeaders.AUTHORIZATION, 
					p.getBasic()+" "+c.codificador(p.getUserPswdOSC()));
	}//setHeader()
	
	public void setHeader(HttpClient client, HttpPost post) throws ClientProtocolException, IOException {
		
		post.setHeader(HttpHeaders.AUTHORIZATION, 
				p.getBasic()+" "+c.codificador(p.getUserPswdOSC()));
		HttpResponse response= client.execute(post);
		
		System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());		
	}//setHeader()

	public boolean checkUser(ContactoOSC contacto, String email) throws Exception {
	
	AwsServiceOSC servicio= new AwsServiceOSC();

	if(servicio.getUser(email)[0].getEmailAddress()==contacto.getEmailAddress()) {
		
		return true;	
	}//if
	return false;
	}//checkUser()
	
	public String serializeContact(String contacto) throws ClientProtocolException, IOException {
		
		ContactoOSC contactoOSC = new ContactoOSC();
		ObjectMapper obj = new ObjectMapper();
		JsonNode node = obj.readTree(contacto);
		contactoOSC.setFirstName(node.get(UriTransformer.JsonTransformerJSON("firstname")).asText());
		contactoOSC.setLastName(node.get(UriTransformer.JsonTransformerJSON("lastname")).asText());
		contactoOSC.setEmailAddress(node.get(UriTransformer.JsonTransformerJSON("emailaddress")).asText());
		
		return obj.writeValueAsString(contactoOSC);
   	}
	
public String serializeLead(Lead lead) throws ClientProtocolException, IOException {
						
		ObjectMapper obj= new ObjectMapper();
		return obj.writeValueAsString(lead);
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
	
	public String deserializateLead(JSONObject objeto) throws Exception{
	    
		Lead lead= new Lead();
		ObjectMapper obj = new ObjectMapper();
		JsonNode node = obj.readTree(objeto.toString());
		lead.setLeadId(node.get("LeadId").asText());
		String id= lead.getLeadId();
		System.out.println("ID: " + id);
		return id;
	}

	public boolean checkUserExist(String contacto) throws Exception {
		
		AwsServiceOSC servicio= new AwsServiceOSC();
		
		ContactoOSC c = new ContactoOSC();
		JSONObject object = new JSONObject(contacto);
		c.setFirstName(object.getString("firstname"));
		c.setLastName(object.getString("lastname"));
		c.setEmailAddress(object.getString("emailaddress"));
		
		if(servicio.getUser(c.getEmailAddress())[0].getEmailAddress()!=null) {
			return true;	
		}//if
		return false;
	}//checkUser()

	
	public boolean checkUserSave(ContactoOSC contacto) throws Exception {
		
		
		AwsServiceOSC servicio= new AwsServiceOSC();
		
		if(servicio.getUser(contacto.getEmailAddress())[0].getEmailAddress()!=null) {
			return true;	
		}//if
		return false;
	}//checkUser()
	
	public void addLead(String email) throws Exception {
		
		String url=p.createLead();

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request= new HttpPost(url);
        
        ContactoOSC[] c= getUser(email);
        		
        Lead lead= new Lead();
        lead.setContactPartyNumber(c[0].getPartyNumber());
        lead.setName(c[c.length-1].getEmailAddress()+"-"+"Lead");
        
        System.out.println(serializeLead(lead));
        StringEntity entity = new StringEntity(serializeLead(lead),
                ContentType.APPLICATION_JSON);
        
        request.setEntity(entity);
        setHeader(client, request);
        
	}//addLead()
	
	
}//AwsServiceOSC