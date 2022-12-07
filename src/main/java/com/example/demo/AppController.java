package com.example.demo;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@RestController
@CrossOrigin("http://localhost:4200")
public class AppController {
	
	@Autowired
	LoginRepository loginRepo;

	@PostMapping("authenticate")
	public ResponseEntity doAuthenticate(@RequestBody Login login) {
		AuthenticationResponse ar=new AuthenticationResponse();
		ar.setStatus(false);
		try {
			Login l= loginRepo.findById(login.getUsername()).get();	
			if(l.getPassword().equals(login.getPassword())) {
				String token=JWT.create().withSubject(l.getUsername())
						.withIssuedAt(new Date(System.currentTimeMillis()))
						.withExpiresAt(new Date(System.currentTimeMillis()+1000*60*10))
						.sign(Algorithm.HMAC256("glob"));
				HttpHeaders headers=new HttpHeaders();
				headers.set("Authorization", "Bearer "+token);
				ar.setStatus(true);
				ar.setRoles(l.getRoles());
				ar.setUserid(l.getUserid());
				ar.setUsername(l.getUsername());
				return new ResponseEntity<AuthenticationResponse>(ar,headers,HttpStatus.OK);
				
			}
			else {
				return new ResponseEntity<AuthenticationResponse>(ar,HttpStatus.FORBIDDEN);
			}
			
		}catch(Exception e) {
			return new ResponseEntity<AuthenticationResponse>(ar,HttpStatus.NOT_FOUND);
			
		}			
		
	}
	
	@GetMapping("validate")
	public ResponseEntity doValidate(@RequestHeader(name="Authorization") String authToken) {
		String token=authToken.substring("Bearer ".length());
		
		try {
			Algorithm alg=Algorithm.HMAC256("glob");
			JWTVerifier verifier=JWT.require(alg).build();
			
			DecodedJWT decode=verifier.verify(token);
			
			String username=decode.getSubject();
			
			return new ResponseEntity(username,HttpStatus.OK);
		}
		catch(Exception e) {
			return new ResponseEntity("False",HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("showuser")
	public ResponseEntity <List<Login>> showAllUser(){
		
		return new ResponseEntity <List<Login>>(loginRepo.findAll(),HttpStatus.OK);
		
	}
		

}
