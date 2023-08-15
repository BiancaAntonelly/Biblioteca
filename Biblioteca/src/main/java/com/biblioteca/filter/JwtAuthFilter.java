package com.biblioteca.filter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtAuthFilter extends GenericFilterBean {
	//GenericFilterBean é uma classe base fornecida pelo Spring para criar filtros

    private final KeyPair keyPair = generateKeyPair();

    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException("Error generating key pair", e);
        }
    }
    //usando o metodo generateKeyPair ele cria um par de chaves

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = request.getHeader("Authorization");
        //obtem o token pelo cabeçalho
        
        if (token != null && token.startsWith("Bearer ")) {
        	//verifico se tem o prefixo Bearer
            token = token.substring(7);
            //se tiverr remove elepara tere só token
            try {
                PublicKey publicKey = keyPair.getPublic();
               
                JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(publicKey).build();
                Claims claims = jwtParser.parseClaimsJws(token).getBody();
                

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        claims.getSubject(), null, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                // Token inválido
                // Você pode tratar o erro aqui, como retornar uma resposta de erro 401
                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
