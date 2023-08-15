package com.biblioteca.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.models.Livro;
import com.biblioteca.services.LivroService;
import com.biblioteca.services.UsuarioService;

@RestController
@RequestMapping("/livros")
public class LivroRestController {

	    
	    private final LivroService livroService;
	    //private final UsuarioService usuarioService;
	    
	    @Autowired
	    //serão injetadas automaticamente pelo spring
	    public LivroRestController(LivroService livroService, UsuarioService usuarioService) {
	        this.livroService = livroService;
	        //this.usuarioService = usuarioService;
	    }

	    
	    @PostMapping("")
	    public ResponseEntity<String> criarLivro(@RequestBody Livro livro) {
	    	System.out.println("entrando em criar livro controller");
	        Livro novoLivro = livroService.criarLivro(livro);
	        if(novoLivro!=null) {
	        String mensagem = "Livro criado com sucesso: " + novoLivro.getNomeLivro();
	        return ResponseEntity.status(HttpStatus.CREATED).body(mensagem);
	        }else {
	            String mensagemErro = "Não foi possível criar o livro.";
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mensagemErro);
	        }
	        //cria um novo livro
	    }

	    @PutMapping("editar/{id}")
	    public ResponseEntity<Livro> editarLivro(@PathVariable Long id, @RequestBody Livro livroAtualizado) {
	        Livro livroEditado = livroService.editarLivro(id, livroAtualizado);
	        if (livroEditado != null) {
	            return ResponseEntity.ok(livroEditado);
	            // Verifica se o livro foi editado com sucesso e retorna o livro editado
	        } else {
	            return ResponseEntity.notFound().build();
	            // Retorna erro 404 caso o livro não seja encontrado
	        }
	    }

	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> excluirLivro(@PathVariable Long id) {
	        livroService.excluirLivro(id);
	        return ResponseEntity.noContent().build();
	    }
}


