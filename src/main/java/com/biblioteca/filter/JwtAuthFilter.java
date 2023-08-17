package com.biblioteca.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private String jwtSigningKey = "minhachavesecretaminhachavesecretaminhachavesecretaminhachavesecretaminhachavesecreta";

    //GenericFilterBean é uma classe base fornecida pelo Spring para criar filtros

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
//    	criamos um metódo para obter uma chave de assinatura
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
//      decodifica a chave JWT em bytes usando a classe Decoders
        return Keys.hmacShaKeyFor(keyBytes);
//      uso a classe Keys para criar uma assinatura usado bytes da chave códificada acima
    }
}
