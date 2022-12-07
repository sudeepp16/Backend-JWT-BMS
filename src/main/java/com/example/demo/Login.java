
package com.example.demo;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Login {

	@Id
	private String username;
	private String password;
	private String roles;
	private int userid;
	
	
}
