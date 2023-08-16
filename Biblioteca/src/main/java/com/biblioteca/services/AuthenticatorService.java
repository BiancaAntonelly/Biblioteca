package com.biblioteca.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.biblioteca.models.Auth;
import com.biblioteca.models.Usuario;
import com.biblioteca.repositories.UsuarioRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


import java.security.Key;
import java.security.KeyPair;
import java.util.Date;

import java.util.Optional;

@Service
public class AuthenticatorService {

    private UsuarioRepository usuarioRepository;
    
    private String jwtSigningKey = "minhachavesecretaminhachavesecretaminhachavesecretaminhachavesecretaminhachavesecreta";

    @Autowired
    public AuthenticatorService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    public String authenticateUser(Auth auth) {
        System.out.println("Entrou no método authenticateUser");
        Optional<Usuario> storedUser = usuarioRepository.findByUsername(auth.getUsername());

        if (storedUser.isPresent()) {
            Usuario usuario = storedUser.get();
            boolean passwordMatches = passwordEncoder.matches(auth.getPassword(), usuario.getPassword());

            if (passwordMatches) {
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
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
