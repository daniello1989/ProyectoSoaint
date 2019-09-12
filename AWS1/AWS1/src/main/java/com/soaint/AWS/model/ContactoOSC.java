package com.soaint.AWS.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ContactoOSC {

	@JsonIgnore
	private String partyNumber;
	
	@JsonProperty("FirstName")
	private String firstName;
	
	@JsonProperty("LastName")
	private String lastName;
	
	@JsonProperty("EmailAddress")
	private String emailAddress;
	
	

	public ContactoOSC(String firstName, String emailAddress) {
		super();
		this.firstName = firstName;
		this.emailAddress = emailAddress;
	}

	public ContactoOSC() {
	}
	public String getPartyNumber() {
		return partyNumber;
	}

	public void setPartyNumber(String partyNumber) {
		this.partyNumber = partyNumber;
	}

	@JsonProperty("FirstName")
	public String getFirstName() {
		return firstName;
	}

	@JsonProperty("FirstName")
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@JsonProperty("LastName")
	public String getLastName() {
		return lastName;
	}

	@JsonProperty("LastName")
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@JsonProperty("EmailAddress")
	public String getEmailAddress() {
		return emailAddress;
	}

	@JsonProperty("EmailAddress")
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	@Override
	public String toString() {
		return "ContactoOSC1 [partyNumber=" + partyNumber + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", emailAddress=" + emailAddress + "]";
	}
	
	
}
