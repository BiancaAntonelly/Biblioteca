package com.biblioteca.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.models.Auth;
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
    public ResponseEntity<String> authenticate(@RequestBody Auth auth) {
    	System.out.println("Entrou no método authenticate");
    	//recebe um objeto auth
    	String authResult = authenticatorService.authenticateUser(auth);
        // Chame o serviço do metodo authenticateUser para verificar as  informações do usuario
        if (authResult.startsWith("Autenticação bem sucedida")) {
        	//startWith verifica se o inicio da frase começa com isso
        	//caso comesse ele gera o token
            final String token = authenticatorService.generateToken(auth.getUsername());
            //gera um token usando o authenticateService
            auth.setToken(token);
            //define o token no objeto auth
        } 
        return ResponseEntity.ok(authResult);
    }
}


