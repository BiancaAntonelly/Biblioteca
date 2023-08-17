package com.biblioteca.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import com.biblioteca.models.Usuario;
import com.biblioteca.repositories.UsuarioRepository;



@Service
public class UsuarioService {
	private final UsuarioRepository usuarioRepository;

    @Autowired
    //injeta uma instancia do usuarioRepository no UsuaroService
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    
    
    private final BCryptPasswordEncoder senhaOriginal = new BCryptPasswordEncoder();

    

    public Usuario criarUsuario(Usuario usuario) {
    	if (usuario.getUsername() == null || usuario.getUsername().length() < 3) {
    		return null;
        }
    	
    	String encodedPassword = senhaOriginal.encode(usuario.getPassword());
    	//a senha é codificada
        usuario.setPassword(encodedPassword);
        //seto o usuario para que o usuario seja salvo a senha codificada
        
        return usuarioRepository.save(usuario);
    }

    public Usuario editarUsuario(Long id, Usuario usuarioAtualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(id).orElse(null);
        //procura o usuaro atravez do findById e devolve se encontrou ou vazio caso não tenha encontrado
        if (usuarioExistente != null) {
            usuarioExistente.setUsername(usuarioAtualizado.getUsername());
            String encodedPassword = senhaOriginal.encode(usuarioAtualizado.getPassword());
            usuarioExistente.setPassword(encodedPassword);
       
            return usuarioRepository.save(usuarioExistente);
        } else {
            return null;
        }
    }




    public void excluirUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
    
 // Método para obter um usuário pelo ID
    //uso no LivrorestController
    public Usuario getUsuarioById(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }
}
