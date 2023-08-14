package com.biblioteca.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.biblioteca.models.Auth;
import com.biblioteca.models.Usuario;
import com.biblioteca.repositories.UsuarioRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


import java.security.Key;
import java.util.Date;

import java.util.Optional;

@Service
public class AuthenticatorService {

    

    private UsuarioRepository usuarioRepository;
    
    @Autowired
    public AuthenticatorService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    //Cria um objeto BCryptPasswordEncoder para codificar senhas
    //É uma classe oferecida pelo spring security
    //Essa clase implementa a interface passwordEncoder que verifica e codifica senhas seguras
    
    //private final String secret = "chave-secreta-token";
    //assinatura do token
    
    public String authenticateUser(Auth auth) {
    	System.out.println("Entrou no método authenticateUser");
    	Optional<Usuario> storedUser = usuarioRepository.findByUsername(auth.getUsername());
    	//buca no repositorio de usuarioRepository um usuario com o username
        //caso exista um usuario ele é salvo em um optional
    	if (storedUser.isPresent()) {
    	//verifica se optional contém um usuario
            Usuario usuario = storedUser.get();
            //se tiver um usuario no optional ele atribui esse usuario a variavel usuario
            boolean passwordMatches = passwordEncoder.matches(auth.getPassword(), usuario.getPassword());
            //compara a senha do usuario com a senha do auth
            if (passwordMatches) {
            	//se a senha for igual
            	String token = generateToken(auth.getUsername());
            	return "Autenticação bem sucedida no usuário: " + auth.getUsername() + "\nToken: " + token;
            } else {
            	return "Problema na autenticação do usuário: " + auth.getUsername();
            }
        } else {
        	return "Usuário não encontrado";
        }
    }
    

    public String generateToken(String username) {
    	//recebe o nome do usuario
    	   Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    	   //cria uma chave secreta key que é usada para assinar os tokens
        return Jwts.builder()
                .setSubject(username)
                //define o nome so usuario passado como argumento
                .setIssuedAt(new Date())
                //define o momento que o token foi criado
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                //define o momento que o token vai expirar
                .signWith(key)
                //indica que a chave do token será feita usando a chave key que foi criada
                .compact();
        		//gera o token final e retorna 

    }
}









