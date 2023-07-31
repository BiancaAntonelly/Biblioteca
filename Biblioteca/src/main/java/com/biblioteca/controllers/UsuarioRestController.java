package com.biblioteca.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.models.Usuario;
import com.biblioteca.services.UsuarioService;

@RestController
//indica que a classe é um controlador restful
@RequestMapping("/usuario")
//fala que as solicitações /usuario são dessa classe
public class UsuarioRestController {
	
	@Autowired
	//faz uma injeção de dependência em usuarioService na classe UsuarioService
	private UsuarioService usuarioService;
	
	
	@PostMapping("")
    public ResponseEntity<?> criarUsuario(@RequestBody Usuario usuario) {
        Usuario novoUsuario = usuarioService.criarUsuario(usuario);

        if (novoUsuario != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
        } else {
            String mensagemErro = "O nome do usuário deve ter pelo menos 3 letras.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensagemErro);
        }
    }
   

    @PutMapping("editar/{id}")
    public ResponseEntity<Usuario> editarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioAtualizado) {
        //@PathVariable Long id significa que o id vai ser retirado do caminho http
    	Usuario usuarioEditado = usuarioService.editarUsuario(id, usuarioAtualizado);
        if (usuarioEditado != null) {
            return ResponseEntity.ok(usuarioEditado);
            //ocorreu tudo certo
        } else {
            return ResponseEntity.notFound().build();
            //usuario não encontrado, notFound é o metodo do ResponseEntity, erro 404
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirUsuario(@PathVariable Long id) {
        usuarioService.excluirUsuario(id);
        return ResponseEntity.noContent().build();
    }

}
