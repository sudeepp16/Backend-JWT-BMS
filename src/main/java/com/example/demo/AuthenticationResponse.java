package com.example.demo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AuthenticationResponse {
	private int userid;
	private String roles;
	private String username;
	private boolean status;
	
}
