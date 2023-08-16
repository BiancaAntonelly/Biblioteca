package com.biblioteca.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.util.ArrayList;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private String jwtSigningKey = "minhachavesecretaminhachavesecretaminhachavesecretaminhachavesecretaminhachavesecreta";

    //GenericFilterBean é uma classe base fornecida pelo Spring para criar filtros

    private final KeyPair keyPair = generateKeyPair();
    //a variável keyPair é inicializada com o resultado da chamada

    private KeyPair generateKeyPair() {
        //metodo privado só pode ser chamado dentro desta classe
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            //cria uma instância de KeyPairGrenerator usando a criptografia RSA
            //.getIntance("RSA") Este é um método estatico da classe KeyPairGnerator
            //usado para gerar pares de chaves
            keyPairGenerator.initialize(2048);
            //inicializa o gerador de chave com tamanho da chave 2048 bits
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException("Error generating key pair", e);
        }
    }
    //usando o metodo generateKeyPair ele cria um par de chaves

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                JwtParser jwtParser = Jwts.parserBuilder()
                        .setSigningKey(getSigningKey())
                        .build();
                Claims claims = jwtParser.parseClaimsJws(token).getBody();
                String username = claims.getSubject();
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username, null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
