package com.soaint.security;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertiesRead {
	static Properties p;
	
	public PropertiesRead() throws FileNotFoundException, IOException {
		
		iniciar();
	}

	   //*********RIGHT NOW*******************
	   public static String getURLRightNow(){
	       return p.getProperty("UrlRightNow").toString();
	   }
	   
	   //*********ELOQUA*******************
	   public static String getURLEloqua(){
	       return p.getProperty("urlEloqua").toString();
	   }
	   
	   public static String getUserPswdEloqua(){
	       return p.getProperty("userPaswdEloqua").toString();
	   }
	   
	   //*********OSC*******************
	   public static String getURLOSC(){
	       return p.getProperty("urlOSC").toString();
	   }
	   
	   public static String getUserPswdOSC(){
	       return p.getProperty("userPasswdOSC").toString();
	   }
	   
	   public static String saveUserOSC() {
		   return p.getProperty("urlSaveOSC").toString();
	   }
	
	   public void iniciar() throws FileNotFoundException, IOException {
		   this.p= new Properties();
		   p.load(new FileReader("src//main//resources//application.properties"));
	}
}//PropertiesRead
