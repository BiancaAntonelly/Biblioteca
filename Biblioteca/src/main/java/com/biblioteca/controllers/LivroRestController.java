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

import com.biblioteca.models.Livro;
import com.biblioteca.models.Usuario;
import com.biblioteca.services.LivroService;
import com.biblioteca.services.UsuarioService;

@RestController
@RequestMapping("/livros")
public class LivroRestController {

	    
	    private final LivroService livroService;
	    private final UsuarioService usuarioService;
	    
	    @Autowired
	    //serão injetadas automaticamente pelo spring
	    public LivroRestController(LivroService livroService, UsuarioService usuarioService) {
	        this.livroService = livroService;
	        this.usuarioService = usuarioService;
	    }

	    
	    @PostMapping("")
	    public ResponseEntity<Livro> criarLivro(@RequestBody Livro livro) {
	    	//é uma classe do Spring Framework que representa uma resposta HTTP personalizada
	        if (livro.getUsuario() == null) {
	            Usuario usuarioBiblioteca = usuarioService.getUsuarioById(Usuario.USUARIO_BIBLIOTECA_ID);
	            livro.setUsuario(usuarioBiblioteca);
	            //caso o usuario seja nulo o usuaro que fica para ele é o padrão da biblioteca
	        } else {
	            Usuario usuario = usuarioService.getUsuarioById(livro.getUsuario().getId());
	            livro.setUsuario(usuario);
	        }

	        Livro novoLivro = livroService.criarLivro(livro);
	        return ResponseEntity.status(HttpStatus.CREATED).body(novoLivro);
	        //cria um novo livro
	    }



	    @PutMapping("editar/{id}")
	    public ResponseEntity<Livro> editarLivro(@PathVariable Long id, @RequestBody Livro livroAtualizado) {
	        Livro livroEditado = livroService.editarLivro(id, livroAtualizado);
	        if (livroEditado != null) {
	            return ResponseEntity.ok(livroEditado);
	            //verfica se o livro foi editado com sucesso
	        } else {
	            return ResponseEntity.notFound().build();
	            //mostra um erro 404
	        }
	    }

	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> excluirLivro(@PathVariable Long id) {
	        livroService.excluirLivro(id);
	        return ResponseEntity.noContent().build();
	    }

	    @PostMapping("/emprestimo/{livroId}")
	    public ResponseEntity<String> realizarEmprestimo(@PathVariable Long livroId, @RequestBody Long userId) {
	        try {
	            Livro livro = livroService.getLivroById(livroId);
	            //procura o livro com o id especifico
	            Usuario usuario = usuarioService.getUsuarioById(userId);
	            //procura o usuario com o id especifico

	            if (livro == null) {
	                return ResponseEntity.notFound().build();
	                //se o livro for nullo, não existir retorna erro 404
	            }

	            if (livro.isEmprestado()) {
	                return ResponseEntity.badRequest().body("Livro indisponível para empréstimo.");
	                //livro já tenha sido emprestado
	            }

	            livro.setEmprestado(true);
	            livro.setUsuario(usuario); // Associa o livro ao novo usuário
	            livroService.editarLivro(livroId, livro);

	            String mensagem = "Empréstimo realizado com sucesso!\n"
                        + "Data do empréstimo: 24/07/2023;\n"
                        + "Prazo de devolução: 24/08/2024.";
	            return ResponseEntity.status(HttpStatus.CREATED).body(mensagem);
	            //retorna um http com  a mensagem
	        } catch (Exception e) {
	            return ResponseEntity.badRequest().body("Erro ao realizar empréstimo.");
	        }
	    }


}


