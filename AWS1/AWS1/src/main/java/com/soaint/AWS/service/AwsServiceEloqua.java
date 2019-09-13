package com.soaint.AWS.service;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.soaint.AWS.model.AddressType;
import com.soaint.AWS.model.Contacto;
import com.soaint.AWS.model.ContactoEloqua;
import com.soaint.AWS.model.Emails;
import com.soaint.AWS.model.Name;
import com.soaint.AWS.verificadores.VerifierEloqua;
import com.soaint.encoder.Coder;
import com.soaint.repository.IDao;
import com.soaint.security.PropertiesRead;
import com.soaint.security.PropertiesReader;
import com.soaint.serializers.SerializerEloqua;
import com.soaint.transformer.UriTransformer;

@Service
public class AwsServiceEloqua implements IDao{

	//Attributes
	private UriTransformer transformer;
	private static PropertiesReader p;
	private Coder c;
	private SerializerEloqua serializador; 
	private VerifierEloqua verifier;
	
	public AwsServiceEloqua() throws FileNotFoundException, IOException {
		
		serializador=new SerializerEloqua();
		p=new PropertiesReader();
		verifier= new VerifierEloqua();
	}//CONSTRUCTOR
	
	@Override
	public ContactoEloqua getUser(String email) throws ClientProtocolException, IOException {
		
		String emailTransformed= UriTransformer.JsonTransformerURIEmail(email);
		String url=p.getURLEloqua()+"*"+emailTransformed;
		
		HttpClient client = HttpClientBuilder.create().build();

		HttpGet request= new HttpGet(url);
		setHeader(request);
		HttpResponse response= client.execute(request);
		String resultado= EntityUtils.toString(response.getEntity());
		
		System.out.println(resultado);
		 
		System.out.println("Response Code : "
	                + response.getStatusLine().getStatusCode());
		
        JSONObject jsonObject= new JSONObject(resultado);
	    JSONArray lista=jsonObject.getJSONArray("elements");        

		if(lista.length()>0) {
	    jsonObject=lista.getJSONObject(0);

        Gson gson = new GsonBuilder().create();

        ContactoEloqua contacto= gson.fromJson(String.valueOf(jsonObject), ContactoEloqua.class);
        
		 return contacto;	
		} else {
		return new ContactoEloqua();}
	}//getUser()

	@Override
	public void deleteUser(String id) throws Exception{
		
		String url=p.getURLDeleteEloqua()+id;
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpDelete request= new HttpDelete(url);
		
		setHeader(client, request);	
	}//deleteUser()
	
	@Override
	public void saveUserOther(String contacto) throws Exception, IOException {
		
		if(verifier.checkUserExist(contacto)==false) {
		
			String url=p.getURLPostEloqua();
		
			HttpClient client = HttpClientBuilder.create().build();
			HttpPost request= new HttpPost(url);
			setHeader(client, request);
			
			System.out.println(contacto.toString());
        
			StringEntity entity = new StringEntity(serializador.serializeContact(contacto),
                ContentType.APPLICATION_JSON);

			System.out.println("JSONEND:"+serializador.serializeContact(contacto));
			request.setEntity(entity);
			HttpResponse response = client.execute(request);

			System.out.println("Response Code : "
					+ response.getStatusLine().getStatusCode());
		}//if
	}
	
	@Override
	public void saveUser(String email, String nombre) throws Exception {
		
		String url=p.getURLPostEloqua();
		
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request= new HttpPost(url);
		setHeader(client, request);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        ContactoEloqua contacto= new ContactoEloqua(email, nombre);
        
        if(checkUser(contacto, email)!=true) {
        String ejemplo= gson.toJson(contacto);

        StringEntity entity = new StringEntity(ejemplo,
                ContentType.APPLICATION_JSON);

        System.out.println(ejemplo);
        request.setEntity(entity);
        HttpResponse response = client.execute(request);

        System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());}
	}//saveUser()
	
	//**************************ANOTHER METHODS************************************************
	public void setHeader(HttpClient client, HttpDelete delete) throws ClientProtocolException, IOException{
		
		this.c= new Coder();
		c.codificador(p.getUserPswdEloqua());
		
		delete.setHeader(HttpHeaders.AUTHORIZATION, 
				p.getBasic()+" "+c.codificador(p.getUserPswdEloqua()));
		HttpResponse response= client.execute(delete);
		
		System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());

		System.out.println("Cliente eliminado");
	}//setHeader()
	
	public void setHeader(HttpGet get) throws
	ClientProtocolException, IOException {
		this.c= new Coder();
		c.codificador(p.getUserPswdEloqua());
		
		System.out.println(p.getBasic()+" "+c.codificador(p.getUserPswdEloqua()));
		
		get.setHeader(HttpHeaders.AUTHORIZATION, 
				p.getBasic()+" "+c.codificador(p.getUserPswdEloqua()));
	}//setHeader()
	
	public void setHeader(HttpClient client, HttpPost post) throws ClientProtocolException, IOException {
		this.c= new Coder();
		c.codificador(p.getUserPswdEloqua());
		
		post.setHeader(HttpHeaders.AUTHORIZATION, 
				p.getBasic()+" "+c.codificador(p.getUserPswdEloqua()));
		HttpResponse response= client.execute(post);

	}//setHeader()
	
	public boolean checkUser(ContactoEloqua contacto, String email) throws ClientProtocolException, IOException {
		
		AwsServiceEloqua servicio= new AwsServiceEloqua();
		
		if(servicio.getUser(email).getEmailAddress()==contacto.getEmailAddress()) {
			return true;	
		}//if
		return false;
	}//checkUser()
	
	/*
	public boolean checkUserExist(String contacto) throws Exception {
		
		AwsServiceEloqua servicio= new AwsServiceEloqua();
		
		ContactoEloqua c = new ContactoEloqua();
		JSONObject object = new JSONObject(contacto);
		c.setName(object.getString("firstname"));
		c.setEmailAddress(object.getString("emailaddress"));
		
		//String email=UriTransformer.JsonTransformerURIEmail(servicio.getUser(c.getEmailAddress()).getEmailAddress()).toString();
		
		if(servicio.getUser(c.getEmailAddress()).getEmailAddress().toString()!=null) {
			return true;	
		}//if
		return false;
	}//checkUser()
	
	public boolean checkUserSave(ContactoEloqua contacto) throws ClientProtocolException, IOException {
		
		AwsServiceEloqua servicio= new AwsServiceEloqua();
		
		if(servicio.getUser(contacto.getEmailAddress()).getEmailAddress()!=null) {
			return true;	
		}//if
		return false;
	}//checkUser()
*/
	
}//Class
