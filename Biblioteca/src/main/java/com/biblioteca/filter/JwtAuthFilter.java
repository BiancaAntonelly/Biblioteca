package com.biblioteca.filter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
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
import java.security.PublicKey;
import java.util.ArrayList;

public class JwtAuthFilter extends GenericFilterBean {
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
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
    	//o metodo doFilter é implementado na classe GenericFilterBean
    	
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        //esta linha converte o objeto servletRequest em um objeto HttpServletRequest
        //com o http você pode acessar os cabeçalhos
        String token = request.getHeader("Authorization");
        //obtem o valor do cabeçalho Authorization
        
        if (token != null && token.startsWith("Bearer ")) {
        	//verifico se o token não é nulo e se tem o prefixo Bearer
            token = token.substring(7);
            //se tiver remove ele  para ter só token
            try {
                PublicKey publicKey = keyPair.getPublic();
                //obtem o par de chaves, a chave será usada para validar a assinatura do token
               
                
                JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(publicKey).build();
                //cria um analisador de token JWT usando a chave pública

                Claims claims = jwtParser.parseClaimsJws(token).getBody();
                // a função parseClaimsJwt verifica a assinatura do token
                //getBody() extrai informações do token, nome de usuario, permissões
       
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        claims.getSubject(), null, new ArrayList<>());
                //cria uma instância de autenticação com o nome do usuário (assunto do token)
                //claims.getSubject é o nome do usuario
                //primeiro  null são as credenciais, como o token é uma prova de identidade
                // a lista está vazia, vi que autorizações vazias da permissões completas
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // Define a autenticação no contexto de segurança do Spring, autentica o usuario associado ao token

            } catch (Exception e) {
                // Token inválido
                // Você pode tratar o erro aqui, como retornar uma resposta de erro 401
                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
        //concluiu o filtro
        //recebe dois argumentos qaue são os ojbetos de solicitação e resposta dttp
    }
}
