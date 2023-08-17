package com.biblioteca.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

//import com.biblioteca.models.Auth;
import com.biblioteca.models.Usuario;
import com.biblioteca.repositories.UsuarioRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

import java.util.Optional;

@Service
public class AuthenticatorService {

    private UsuarioRepository usuarioRepository;
    
    private String jwtSigningKey = "minhachavesecretaminhachavesecretaminhachavesecretaminhachavesecretaminhachavesecreta";
//    crio uma chave para usar nas comparações no token, essa chave é usada para a criação e validação do token
    
    @Autowired
    public AuthenticatorService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//    uso para criptografar a senha que foi passada no postman, visto que o a senha do usuario no banco de dados
//    está criptografada, então para ser comparada as duas senhas eu preciso que as duas esteja criptograda
    
    public String authenticateUser(Usuario usuario) {
        Optional<Usuario> storedUser = usuarioRepository.findByUsername(usuario.getUsername());
//    busco o usuario pelo nome no banco de dados, usuario é o que foi passado no postman
// 	  storedUser é o usuario do banco de dados

        if (storedUser.isPresent()) {
//        	retorna true caso tenha o usuario presente no banco de dados
            Usuario storedUsuario = storedUser.get();           
            boolean passwordMatches = passwordEncoder.matches(usuario.getPassword(), storedUsuario.getPassword());
//			nesta linha eu estou comparando as senhas que foi fornecida pelo usuario com a do banco de dados
//          a variavel passwordMatches recebe true caso as senhas estejam iguais
            if (passwordMatches) {
                String token = generateToken(usuario.getUsername());
//                chama o metodo generateToken e passa um usuario como parâmetro
                return "Autenticação bem sucedida no usuário: " + usuario.getUsername() + "\nToken: " + token;
            } else {
                return "A senha do usuário: " + usuario.getUsername() + " está incorreta.";
            }
        } else {
            return "Usuário não encontrado";
        }
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
//                defini o campo Subject com o nome do usuario
                .setIssuedAt(new Date())
//                defini o campo com a data e hora atual
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
//                o token se expira depois de 10 horas de criado
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
//                assino o token com uma chave obtida do metodo getSignindKey
                .compact();
//        		compacta o token em uma forma de string
    }
//	gero o token
    
    private Key getSigningKey() {
//    	criamos um metódo para obter uma chave de assinatura
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
//      decodifica a chave JWT em bytes usando a classe Decoders
        return Keys.hmacShaKeyFor(keyBytes);
//      uso a classe Keys para criar uma assinatura usado bytes da chave códificada acima
    }
}
