package com.auth_api.domain.user;

public class MessageDTO {
	
	private String name;
	
	private String email;
	
	public MessageDTO() {
		
	}

	public MessageDTO(String name, String email) {
		super();
		this.name = name;
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
}
