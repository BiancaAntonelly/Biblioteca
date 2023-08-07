package com.biblioteca.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.models.Auth;
import com.biblioteca.services.AuthenticatorService;
import com.biblioteca.services.LivroService;
import com.biblioteca.services.UsuarioService;

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
    public ResponseEntity<Auth> authenticate(@RequestBody Auth auth) {
    	//recebe um objeto auth
        boolean isAuthenticated = authenticatorService.authenticateUser(auth);
        // Chame o serviço do metodo authenticateUser para verificar as  informações do usuario
        if (isAuthenticated) {
            final String token = authenticatorService.generateToken(auth.getUsername());
            //gera um token usando o authenticateService
            auth.setToken(token);
            //define o token no objeto auth
            return ResponseEntity.ok(auth);
            //retorna uma resposta 
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            //retorna um erro 401, a autenticação falhou
        }
    }
}


