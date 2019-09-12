package com.soaint.AWS.model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.json.JSONPropertyIgnore;

public class Contacto{
	
	@JsonIgnore
	 private String id;
	 private Name 	name;
	 private Emails emails;
	 
	 //Constructor
	 public Contacto() {
	}
	
	// Getter Methods 
	 public String getId() {
		return id;
	 }

	
	 // Setter Methods 
	 public void setId(String id) {
		 this.id = id;
	 }

	 
	public Emails getEmails() {
			return emails;
	}


	public void setEmails(Emails emails) {
		this.emails = emails;
	}

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	
	@Override
	public String toString() {
		return "Contacto [id=" + id + ".]";
	}
}