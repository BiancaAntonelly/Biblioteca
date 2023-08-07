package com.biblioteca.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.biblioteca.models.Auth;
import com.biblioteca.models.Usuario;
import com.biblioteca.repositories.AuthenticatorRepository;
import com.biblioteca.repositories.LivroRepository;
import com.biblioteca.repositories.UsuarioRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Optional;

@Service
public class AuthenticatorService {

    
    private AuthenticatorRepository authenticatorRepository;
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    public AuthenticatorService(AuthenticatorRepository authenticatorRepository, UsuarioRepository usuarioRepository) {
        this.authenticatorRepository = authenticatorRepository;
        this.usuarioRepository = usuarioRepository;
    }

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    //Cria um objeto BCryptPasswordEncoder para codificar senhas
    //É uma classe oferecida pelo spring security
    //Essa clase implementa a interface passwordEncoder que verifica e codifica senhas seguras
    
    private final String secret = "chave-secreta-token";
    //assinatura do token
    
    public boolean authenticateUser(Auth auth) {
        
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
                System.out.println("Autenticação bem sucedida no usuário: " + auth.getUsername());
                return true;
            } else {
                System.out.println("Problema na autenticação do usuário: " + auth.getUsername());
                return false;
            }
        } else {
            System.out.println("Usuário não encontrado");
            return false;
        }
    }

    public String generateToken(String username) {
    	//recebe o nome do usuario
        return Jwts.builder()
                .setSubject(username)
                //define o nome so usuario passado como argumento
                .setIssuedAt(new Date())
                //define o momento que o token foi criado
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                //define o momento que o token vai expirar
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        		//gera o token final e retorna 
    }
}











/*
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticatorService {

    @Autowired
    private UserDetailsService userDetailsService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final String secret = "seu-segredo-aqui";

    public String authenticate(String username, String password) {
        // Implemente a lógica de autenticação aqui
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (passwordMatches(password, userDetails.getPassword())) {
            return generateToken(userDetails);
        }
        return null; // Autenticação falhou
    }


    private boolean passwordMatches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }


    public void processJwtToken(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = extractUsername(jwt);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
    }

    private String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + 1000 * 60 * 60 * 10); // 10 horas

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .compact();
    }

    private boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);
        return expiration.before(new Date());
    }

    private String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    private Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }

    private Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
    
    //comparar a senha
}
*/