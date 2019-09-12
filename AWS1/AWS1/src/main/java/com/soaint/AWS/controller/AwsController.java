package com.soaint.AWS.controller;
import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.JsonObject;
import com.soaint.AWS.model.Contacto;
import com.soaint.AWS.model.ContactoEloqua;
import com.soaint.AWS.model.ContactoOSC;
import com.soaint.AWS.service.AwsService;
import com.soaint.AWS.service.AwsServiceEloqua;
import com.soaint.AWS.service.AwsServiceOSC;

@RestController
@RequestMapping("/index")
public class AwsController {

	@Autowired
	AwsService servicio; 
	@Autowired
	AwsServiceEloqua servicioEloqua;
	@Autowired
	AwsServiceOSC servicioOSC;
	
	//**************************************RN************************************************
    @RequestMapping(value="/rn/delete/{email}", method={RequestMethod.GET})
    public ResponseEntity<String> deleteContact(@PathVariable("email") String email) throws Exception{
    	
    	if(servicio.getUser(email).getId()!=null){
    		servicio.deleteUser(email);
    		return new ResponseEntity<>("El usuario con email "+email+ " ha sido eliminado", HttpStatus.OK);
    	} else 
            return new ResponseEntity<>("El usuario con email "+email+ ", no existe o ya ha sido eliminado =(", HttpStatus.BAD_REQUEST);
    }//deleteContact()    
    
    @RequestMapping(value="/rn/get/{email}", method=RequestMethod.GET)
    public ResponseEntity<String> getContact(@PathVariable("email") String email) throws Exception{
    	
    	if(servicio.getUser(email).getId()!=null){
    		Contacto contacto=servicio.getUser(email);
       return new ResponseEntity<>(contacto.toString(), HttpStatus.OK);
    	} else
    		return new ResponseEntity<>("El usuario con email"+email+" no existe en nuestra plataforma =(", HttpStatus.BAD_REQUEST);
    	}//getContact()
    
    @RequestMapping(value="/rn/getall/", method=RequestMethod.GET)
    public ResponseEntity<String> getAll() throws Exception{
    	
       String resultados= servicio.getAll();
       return new ResponseEntity<>(resultados.toString()+"/n", HttpStatus.OK);
    }//getContact()
    
    @RequestMapping(value="/rn/save/{email}/{nombre}", method= {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<String>saveUserRightNow(@PathVariable("email") String email, 
    		@PathVariable("nombre") String nombre)throws Exception{
    	
    	servicio.saveUser(email, nombre);
    	return new ResponseEntity<>("El nuevo usuario ha sido guardado en caso de no existir en la plataforma.", HttpStatus.OK);
    }//saveUser()
    
    //********************************ELOQUA*************************************
    @RequestMapping(value="eloqua/delete/{email}", method={RequestMethod.GET})
    public ResponseEntity<String> deleteContactEloqua(@PathVariable("email") String email) throws Exception{
    	    	
    		if(servicioEloqua.getUser(email).getId()!=null) {
    			
    			ContactoEloqua contacto=servicioEloqua.getUser(email);
    		       
    			servicioEloqua.deleteUser(contacto.getId());
    			return new ResponseEntity<>("El usuario con el siguiente email, "+email+ ", ha sido eliminado.", HttpStatus.OK);
    		}else return new ResponseEntity<>("El usuario con el siguiente email, "+email+ ", no existe o ya ha sido eliminado =(", HttpStatus.BAD_REQUEST);
    }//deleteContact()    
    
    @RequestMapping(value="eloqua/get/{email}", method=RequestMethod.GET)
    public ResponseEntity<String> getContactEloqua(@PathVariable("email") String email) throws Exception{
    	
       ContactoEloqua contacto=servicioEloqua.getUser(email);
       
       if(servicioEloqua.getUser(email).getId()!=null) {
       return new ResponseEntity<>(contacto.toString(), HttpStatus.OK);}
       else {
           return new ResponseEntity<>("El contacto no existe", HttpStatus.BAD_REQUEST);
           }
    }//getContact()
    
    @RequestMapping(value="/eloqua/save/{email}/{nombre}", method= {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<String>saveUserEloqua(@PathVariable("email") String email,
    		@PathVariable("nombre") String nombre)throws Exception{
    	
    	servicioEloqua.saveUser(email, nombre);
    	return new ResponseEntity<>("El nuevo usuario ha sido guardado en caso de no existir en la plataforma.", HttpStatus.OK);
    }//saveUserEloqua

    //**********************OSC**************************************************
    @RequestMapping(value="osc/delete/{email}", method={RequestMethod.GET})
    public ResponseEntity<String> deleteContactOSC(@PathVariable("email") String email) throws Exception{
    	
    	
    	if(servicioOSC.getUser(email)[0].getPartyNumber()!=null) {

        	ContactoOSC[] contactos= servicioOSC.getUser(email);

    		for(ContactoOSC c: contactos) {
    			servicioOSC.deleteUser(c.getPartyNumber());
    		}

    		return new ResponseEntity<>("Los usuarios con email "+email+ " han sido eliminado", HttpStatus.OK);
    	}	
    	else return new ResponseEntity<>("No existe ningún usuario con email "+email+ " =(", HttpStatus.BAD_REQUEST);
    }//deleteContact()    
    
    @RequestMapping(value="osc/get/{email}", method=RequestMethod.GET)
    public ResponseEntity<String> getContactOSC(@PathVariable("email") String email) throws Exception{
    
       
       if(servicioOSC.getUser(email)[0].getPartyNumber()!=null) {
           ContactoOSC[] contactos=servicioOSC.getUser(email);

    	   String bf="";
    	   
    	   for(ContactoOSC c: contactos) {
    		   bf= bf+c.toString();
    	   }//for
           	return new ResponseEntity<>(bf, HttpStatus.OK);
       }//if	
       else return new ResponseEntity<>("Los usuarios con email "+email+ " no existen en la plataforma =(", HttpStatus.BAD_REQUEST);
    }//getContact()3
    
    @RequestMapping(value="/osc/save/{email}/{nombre}", method= {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<String>saveUserOSC(@PathVariable("email") String email, @PathVariable("nombre") String nombre)throws Exception{
    	
    	servicioOSC.saveUser(email, nombre);
    	return new ResponseEntity<>("El nuevo usuario ha sido guardado en caso de no existir.", HttpStatus.OK);
    }//saveOSCUser()
    
    //*****************************DELETEALL********************************************
    @RequestMapping(value="/all/{email}", method={RequestMethod.GET})
    public ResponseEntity<String> deleteAllContac(@PathVariable("email") String email) throws Exception{
    	    
    		if(servicio.getUser(email).getId()!=null) {
        		servicio.deleteUser(email);
    		} 
    		
    		if(servicioEloqua.getUser(email).getId()!=null) {
    			
    			ContactoEloqua contacto=servicioEloqua.getUser(email); 		       
    			servicioEloqua.deleteUser(contacto.getId());

    		} 
    		
    		if(servicioOSC.getUser(email)[0].getPartyNumber()!=null) {
        		servicioOSC.deleteUser(servicioOSC.getUser(email)[0].getEmailAddress());
    		}
    		
        	return new ResponseEntity<>("Si existe, el usuario con email"+email+ " ha sido eliminado de todas las plataformas.", 
        			HttpStatus.OK);    
    }//deleteContact()    
    
    
    
    //*****************************SAVES***********************************************
    
    @RequestMapping(value="/rn/savealternative", consumes="application/json", produces="application/json", method=RequestMethod.POST)
    public ResponseEntity<String> guardarAlternativo(@RequestBody String contacto) throws UnsupportedCharsetException, Exception{

    	servicio.saveUserOther(contacto);
        return new ResponseEntity<>("Se ha guardado correctamente el contacto en el caso de que no exista previamente.",
        		HttpStatus.OK);
    }
    
    @RequestMapping(value="/eloqua/savealternative", consumes="application/json", produces="application/json", method=RequestMethod.POST)
    public ResponseEntity<String> guardarAlternativoEloqua(@RequestBody String contacto) throws IOException, Exception{
    	
    	servicioEloqua.saveUserOther(contacto);
        return new ResponseEntity<>("Se ha guardado correctamente el contacto en el cado de que no exista previamente.",
        		HttpStatus.OK);
    }

    
    @RequestMapping(value="/osc/savealternative", method=RequestMethod.POST)
    public ResponseEntity<String> guardarAlternativoOSC(@RequestBody String contacto) throws UnsupportedCharsetException, Exception{

    	servicioOSC.saveUserOther(contacto);
        return new ResponseEntity<>("Se ha guardado correctamente el contacto, en el caso de que no exista previamente",
        		HttpStatus.OK);
    }    
    
    //*****************************SAVEALL*********************************************
    @RequestMapping(value="/saveall", consumes="application/json", produces="application/json", method=RequestMethod.POST)
    public ResponseEntity<String> saveAll(@RequestBody String contacto) throws Exception{
    	
        	servicio.saveUserOther(contacto);			
	    	servicioEloqua.saveUserOther(contacto);
	    	servicioOSC.saveUserOther(contacto);
	
    	return new ResponseEntity<>("Usuarios guardados correctamente.", HttpStatus.OK);
    }
}//AwsController