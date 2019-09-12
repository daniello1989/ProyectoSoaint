package com.soaint.AWS.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Lead {
	
	@JsonProperty("Name")
	String name;
	
	@JsonProperty("ContactPartyNumber")
	String contactPartyNumber;
	
	@JsonIgnore
	String leadId;

	@JsonProperty("Name")
	public String getName() {
		return name;
	}

	@JsonProperty("Name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("ContactPartyNumber")
	public String getContactPartyNumber() {
		return contactPartyNumber;
	}

	@JsonProperty("ContactPartyNumber")
	public void setContactPartyNumber(String contactPartyNumber) {
		this.contactPartyNumber = contactPartyNumber;
	}

	public String getLeadId() {
		return leadId;
	}

	public void setLeadId(String leadId) {
		this.leadId = leadId;
	}
		

}
