package com.biblioteca.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.biblioteca.models.Auth;
import com.biblioteca.models.Usuario;
import com.biblioteca.services.AuthenticatorService;


@RestController
//defini a classe como um controlador rest, manipula solicitações http
@RequestMapping("/authenticator")
public class AuthenticatorRestController {

	
    private AuthenticatorService authenticatorService;
    
    @Autowired
    //serão injetadas automaticamente pelo spring
    public AuthenticatorRestController(AuthenticatorService authenticatorService ){
        this.authenticatorService = authenticatorService;
    }
    
    @PostMapping("")
    public ResponseEntity<String> authenticate(@RequestBody Usuario usuario) {
    	//recebe um objeto auth
    	String authResult = authenticatorService.authenticateUser(usuario);
        // Chame o serviço do metodo authenticateUser para verificar as  informações do usuario
        return ResponseEntity.ok(authResult);
    }
}


