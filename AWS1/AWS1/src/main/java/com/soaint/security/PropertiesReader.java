package com.soaint.security;
import java.util.ResourceBundle;

public class PropertiesReader {
   private static final ResourceBundle PROPERTIES = ResourceBundle.getBundle("application");
      
   //*********RIGHT NOW*******************
   public static String getURLRightNow(){
       return PROPERTIES.getString("UrlRightNow");
   }
   public static String getDeleteURLRightNow(){
       return PROPERTIES.getString("UrlDeleteRightNow");
   }
   
   public static String getSaveURLRightNow() {
	   return PROPERTIES.getString("UrlSaveRightNow");
   }
      
   //*********ELOQUA*******************
   public static String getURLEloqua(){
       return PROPERTIES.getString("urlEloqua");
   }
   public static String getUserPswdEloqua(){
       return PROPERTIES.getString("userCredentialEloqua");
   }
   
   public static String getURLDeleteEloqua(){
       return PROPERTIES.getString("urlDeleteEloqua");
   }
   
   public static String getURLPostEloqua(){
       return PROPERTIES.getString("urlPostEloqua");
   }
   
   //*********OSC*******************
   public static String getURLOSC(){
       return PROPERTIES.getString("urlOSC");
   }
   
   public static String getURLDeleteOSC(){
	   
	   return PROPERTIES.getString("urlDeleteOSC");
   }
   
   public static String getUserPswdOSC(){
       return PROPERTIES.getString("userCredentialUsr");
   }
   
   public static String getBasic() {
	   return PROPERTIES.getString("userBasic");
   }
   
   public static String saveUserOSC() {
	   return PROPERTIES.getString("urlSaveOSC");
   }

   public String getIncidentsRightNow() {
	   return PROPERTIES.getString("IncidentsRightNow");
   }
   
   public String createLead() {
	   
	   return PROPERTIES.getString("urlcreateLeadOSC");

   }
public String deleteLead() {

	return PROPERTIES.getString("urlDeleteLeadOSC");
}
}  
