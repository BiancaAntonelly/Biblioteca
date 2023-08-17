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
//	a classe OncePerRequestFilter garante que o filtro seja executado somente uma vez por solicitação

    private String jwtSigningKey = "minhachavesecretaminhachavesecretaminhachavesecretaminhachavesecretaminhachavesecreta";
//  uma chave usada para verificar as assinaturas do token
    
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
//    	parâmetro request: ele representa ua solicitação http, ele contém informações como cabeçalho, metodo http
    	
        String token = request.getHeader("Authorization");
//        obtém o valor do cabeçalho Authorization da solicitação http atravéz do método getHeader
//        o valor do cabeçalho é armazenado na variavel token
        if (token != null && token.startsWith("Bearer ")) {
//        	verifica se o token não é nulo e se começa com Bearer 
            token = token.substring(7);
//          se ele tiver o prefixo ele remove 
            try {
                JwtParser jwtParser = Jwts.parserBuilder()
                        .setSigningKey(getSigningKey())
                        .build();
//                JwtParse é uma classe de biblioteca que verifica os tokens
//                define a chave de assinatura que é usada para verificar a validade do token
                Claims claims = jwtParser.parseClaimsJws(token).getBody();
//                o token é analisado usando o ojeto JwtParse
                String username = claims.getSubject();
//                estamos obtendo o nome do usuario
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username, null, new ArrayList<>());
//                aqui criamos uma instancia de UsernamePasswordAuthenticationToken, que é um tipo de autenticação do spring security
//                estamos passando o nome do usuario e um objeto null para as credências, pois o token jwt já foi usado para autenticar
                SecurityContextHolder.getContext().setAuthentication(authentication);
//                configura a autenticação
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                retorna que a autenticação falhou
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
