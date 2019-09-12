package com.soaint.AWS.service;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.nio.charset.UnsupportedCharsetException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.soaint.AWS.model.AddressType;
import com.soaint.AWS.model.Contacto;
import com.soaint.AWS.model.Emails;
import com.soaint.AWS.model.Name;
import com.soaint.encoder.Coder;
import com.soaint.repository.IDao;
import com.soaint.security.PropertiesReader;
import com.soaint.transformer.UriTransformer;


@Service
public class AwsService implements IDao{
	
	private UriTransformer transformer;
	PropertiesReader p;
	private Coder c;
	
	public AwsService() throws FileNotFoundException, IOException {
		p= new PropertiesReader();
	}//constructor
	
	@Override
	public void deleteUser(String email) throws Exception {
		
		Contacto contacto=getUser(email);
	
		String url=p.getDeleteURLRightNow()+contacto.getId();
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpDelete request= new HttpDelete(url);
		
		HttpResponse response = client.execute(request);

		System.out.println("Response Code : "
	                + response.getStatusLine().getStatusCode());

		System.out.println("Cliente eliminado");
	}//deleteUser()
	
	@Override
	public Contacto getUser(String email) throws Exception{
		
		String emailTransformed= UriTransformer.JsonTransformerURIEmail(email);
		
		String url=p.getURLRightNow()+"'"+emailTransformed+"'";
		HttpClient client= HttpClientBuilder.create().build();
		HttpGet request= new HttpGet(url);
		
		HttpResponse response= client.execute(request);
				
		String resultado= EntityUtils.toString(response.getEntity());
		
		System.out.println("Response Code : "
	                + response.getStatusLine().getStatusCode());
		
		JSONObject jsonObject= new JSONObject(resultado);
	    JSONArray lista=jsonObject.getJSONArray("items");
	    
	    if(lista.length()>0) {
	    jsonObject=lista.getJSONObject(0);
	       
	    System.out.println(jsonObject.get("id"));
	        
	    System.out.println(jsonObject.toString());
	       
	    Gson gson = new GsonBuilder().create();
	    Contacto contacto= gson.fromJson(String.valueOf(jsonObject), Contacto.class);
	    System.out.println(contacto.toString());
	        
	    return contacto;}else return new Contacto();
		}//getUser()
	
	//************************TESTING METHODS************************************
	public String getAll() throws Exception{
		
		String url=	p.getURLRightNow();
		
		HttpClient client= HttpClientBuilder.create().build();
		HttpGet request= new HttpGet(url);
		
		HttpResponse response= client.execute(request);
		
		String resultado= EntityUtils.toString(response.getEntity());
		 
		System.out.println("Response Code : "
	                + response.getStatusLine().getStatusCode());
		
		return resultado; 
	}//getAll()
		
	@Override
	public void saveUser(String email, String nombre) throws UnsupportedCharsetException, Exception {
		
		String url=p.getSaveURLRightNow();
		
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request= new HttpPost(url);
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        Contacto contacto=setContacto(email, nombre);

        if(checkUser(contacto, email)!=true) {
        String ejemplo= gson.toJson(contacto);

        System.out.println(ejemplo);
        StringEntity entity = new StringEntity(ejemplo,
                ContentType.APPLICATION_JSON);

        request.setEntity(entity);
        HttpResponse response = client.execute(request);

        System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());
       }
	}//save()
	
	@Override
	public void saveUserOther(String contacto) throws UnsupportedCharsetException, Exception {
		
		//if(checkUserExist(contacto)==true) {
			String url=p.getSaveURLRightNow();
	
		
			HttpClient client = HttpClientBuilder.create().build();
			HttpPost request= new HttpPost(url);
        
			System.out.println(contacto.toString());
        
			StringEntity entity = new StringEntity(serializeContact(contacto),
                ContentType.APPLICATION_JSON);

			System.out.println(serializeContact(contacto));
			request.setEntity(entity);
			HttpResponse response = client.execute(request);

			System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode()); 
		
			//}//if
		}//saveUserOther()

	
	//**********************ANOTHER METHODS**********************
	private Contacto setContacto(String email, String nombre) {
		
		String name= name(nombre, 0);
        String last= name(nombre, 1);
        Name nomb= new Name(name, last);
		Contacto contacto= new Contacto();
        Emails mail= new Emails();
        mail.setAddress(email);
        mail.setAddressType(new AddressType());
        contacto.setEmails(mail);
        contacto.setName(nomb);
		return contacto;
	}//setContacto()
	
	
	public boolean checkUser(Contacto contacto, String email) throws Exception {
		
		AwsService servicio= new AwsService();
		
		if(servicio.getUser(email).getEmails().getAddress()==contacto.getEmails().getAddress()) {
			return true;	
		}//if
		return false;
	}//checkUser()
	
	public String name(String name, int number) {
		
		String[] datos = name.split(" ");
		String dato= datos[number];
		return dato;	
	}

	public String serializeContact(String contacto) throws ClientProtocolException, IOException {
		Contacto contactoRN = new Contacto();
		JSONObject object = new JSONObject(contacto);
		contactoRN.setName(new Name(object.getString(UriTransformer.JsonTransformerJSON("firstname")), 
				object.getString(UriTransformer.JsonTransformerJSON("lastname"))));
		contactoRN.setEmails(new Emails(object.getString(UriTransformer.JsonTransformerJSON("emailaddress")),
				new AddressType()));
		ObjectMapper obj = new ObjectMapper();
		return obj.writeValueAsString(contactoRN).toString();
   	}
	
	
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

}//class