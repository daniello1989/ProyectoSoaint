package com.soaint.AWS.model;

public class ContactoEloqua {
	  //private String type;
	  private String id;
	  private String createdAt;
	  private String depth;
	  private String name;
	  private String updatedAt;
	  private String emailAddress;
	  
	 public ContactoEloqua(String type, String id, String createdAt, String depth, String name, String updatedAt,
			String emailAddress) {
		super();
		this.id = id;
		this.createdAt = createdAt;
		this.depth = depth;
		this.name = name;
		this.updatedAt = updatedAt;
		this.emailAddress = emailAddress;
	}
	 
	public ContactoEloqua(String emailAddress, String name) {
		this.emailAddress=emailAddress;
		this.name=name;
	}

	public ContactoEloqua() {
	}

	// Getter Methods 
	/* 
	public String getType() {
	    return type;
	  }
	 */
	  public String getId() {
	    return id;
	  }

	  public String getCreatedAt() {
	    return createdAt;
	  }

	  public String getDepth() {
	    return depth;
	  }

	  public String getName() {
	    return name;
	  }

	  public String getUpdatedAt() {
	    return updatedAt;
	  }

	  public String getEmailAddress() {
	    return emailAddress;
	  }

	 // Setter Methods 

	  /*
	  public void setType( String type ) {
	    this.type = type;
	  }
	   */
	  public void setId( String id ) {
	    this.id = id;
	  }

	  public void setCreatedAt( String createdAt ) {
	    this.createdAt = createdAt;
	  }

	  public void setDepth( String depth ) {
	    this.depth = depth;
	  }

	  public void setName( String name ) {
	    this.name = name;
	  }

	  public void setUpdatedAt( String updatedAt ) {
	    this.updatedAt = updatedAt;
	  }

	  public void setEmailAddress( String emailAddress ) {
	    this.emailAddress = emailAddress;
	  }

	  @Override
	  public String toString() {
		  return "ContactoEloqua [, id=" + id + ", createdAt=" + createdAt + ", depth=" + depth
				  + ", name=" + name + ", updatedAt=" + updatedAt + ", emailAddress=" + emailAddress + "]";
	  }//toString()
	}//class