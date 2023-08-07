package com.biblioteca.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Auth {
	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
	    private String username;
	    private String password;
	    private String token;

	    //construtores:
	    
	    public Auth() {
	    }
	    public Auth(Long id, String username, String password, String token) {
			this.id = id;
			this.username = username;
			this.password = password;
			this.token = token;
		}

		//gets e sets
	    
	    public Long getId() {
	    	return id;
	    }
	    public void setId(Long id) {
	    	this.id = id;
	    }
		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

	    
	}


