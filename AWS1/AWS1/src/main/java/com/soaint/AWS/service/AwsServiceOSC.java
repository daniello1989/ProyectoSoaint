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
import com.soaint.AWS.verificadores.VerifierOSC;
import com.soaint.deserializer.DeserializerLeadOSC;
import com.soaint.deserializer.DeserializerOSC;
import com.soaint.encoder.Coder;
import com.soaint.repository.IDao;
import com.soaint.security.PropertiesRead;
import com.soaint.security.PropertiesReader;
import com.soaint.serializers.SerializerLeadOSC;
import com.soaint.serializers.SerializerOSC;
import com.soaint.transformer.UriTransformer;

@Service
public class AwsServiceOSC implements IDao{
	
	//Attributes
	private UriTransformer transformer;
	private static PropertiesReader p;
	private Coder c;
	private SerializerOSC serializer;
	private DeserializerOSC deserializer;
	private SerializerLeadOSC serializerLead;
	private DeserializerLeadOSC deserializerLead;
	private VerifierOSC verifier;
	
	public AwsServiceOSC() throws FileNotFoundException, IOException {
		serializer= new SerializerOSC();
		deserializer= new DeserializerOSC();
		serializerLead= new SerializerLeadOSC();
		deserializerLead= new DeserializerLeadOSC();
		
		p=new PropertiesReader();
		verifier= new VerifierOSC();
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

	    contactoOSC=deserializer.deserializeContact(jsonObject);
	    
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
	
	public String getLead(String PartyNumber) throws Exception {
		
		System.out.println("PARTY NUMBER: "+PartyNumber);
		String url="https://adc4-zgth-fa-ext.oracledemos.com/crmRestApi/resources/latest/leads?q=ContactPartyNumber="
		+PartyNumber;
		
		HttpClient client= HttpClientBuilder.create().build();
		HttpGet request= new HttpGet(url);
		
		setHeader(request);
		
		HttpResponse response= client.execute(request);
		String resultado= EntityUtils.toString(response.getEntity());
		
		System.out.println("************RESULTADO*****************:"+resultado);
	   
	    JSONObject jsonObject= new JSONObject(resultado);
	    JSONArray lista=jsonObject.getJSONArray("items");

	    if(lista.length()>=1) {
	    System.out.println(jsonObject);
	   
	    return deserializerLead.deserializateLead(lista.getJSONObject(0));
	    }else 
	    	return "";
	    }
	
	public void addLead(String email) throws Exception {
		
		String url=p.createLead();

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request= new HttpPost(url);
        
        ContactoOSC[] c= getUser(email);
        		
        Lead lead= new Lead();
        lead.setContactPartyNumber(c[0].getPartyNumber());
        lead.setName(c[c.length-1].getEmailAddress()+"-"+"Lead");
        
        StringEntity entity = new StringEntity(serializerLead.serializeLead(lead),
                ContentType.APPLICATION_JSON);
        
        System.out.println("AÑADIENDOOOOOOOOOOOOOOOOOOOO");
        request.setEntity(entity);
        setHeader(client, request);
        
	}//addLead()



	@Override
	public void saveUser(String email, String nombre) throws Exception {
	
		String url=p.saveUserOSC();
		
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request= new HttpPost(url);
        setHeader(client, request);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        ContactoOSC contacto= new ContactoOSC(email, nombre);
        
        
        if(verifier.checkUser(contacto, email)!=true){
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
		
		if(verifier.checkUserExist(contacto)==true) {
			System.out.println("Funciono1");
		
			if(verifier.checkLeadExists(contacTransformed)==false) {
				
			System.out.println("FUNCIONO =)");
			saveLeadWithOutUser(contacto);
			}
		}
		
		if(verifier.checkUserExist(contacto)==false) {
		String url=p.saveUserOSC();
		
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request= new HttpPost(url);

        System.out.println(contacto.toString());
        
        System.out.println("JSONSEND: " + serializer.serializeContact(contacto));
            StringEntity entity = new StringEntity(serializer.serializeContact(contacTransformed),
                    ContentType.APPLICATION_JSON);
          
            System.out.println("No funciono");
        request.setEntity(entity);
        setHeader(client, request);
       
		ContactoOSC contactoOSC = new ContactoOSC();
		ObjectMapper obj = new ObjectMapper();
		JsonNode node = obj.readTree(contacto);
		contactoOSC.setEmailAddress(node.get(UriTransformer.JsonTransformerJSON("emailaddress")).asText());

       // addLead(contactoOSC.getEmailAddress());
		}
	}
	
	public void saveLeadWithOutUser(String contacto) throws Exception{
		
		ContactoOSC contactoOSC = new ContactoOSC();
		ObjectMapper obj = new ObjectMapper();
		JsonNode node = obj.readTree(contacto);
		contactoOSC.setEmailAddress(node.get(UriTransformer.JsonTransformerJSON("emailaddress")).asText());
		
        addLead(contactoOSC.getEmailAddress());
	}//saveLeadWithOutUser()
	
	public void setHeader(HttpClient client, HttpDelete delete) throws ClientProtocolException, IOException{
				
		delete.setHeader(HttpHeaders.AUTHORIZATION, 
				p.getBasic()+" "+c.codificador(p.getUserPswdOSC()));
		HttpResponse response= client.execute(delete);
		
		System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());

		System.out.println("Cliente eliminado");
	}//setHeader()
	
	public void setHeader(HttpGet get) throws ClientProtocolException, IOException {
				
		
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
}//AwsServiceOSC