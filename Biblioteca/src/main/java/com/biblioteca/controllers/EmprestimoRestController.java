package com.biblioteca.controllers;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.biblioteca.models.Emprestimo;
import com.biblioteca.models.Livro;
import com.biblioteca.services.EmprestimoService;
import com.biblioteca.services.LivroService;

@RestController
//indica que a classe é um controlador rest
@RequestMapping("/emprestimos")
//mapeia as url
public class EmprestimoRestController {

	
    private final EmprestimoService emprestimoService;
    private final LivroService livroService;

    @Autowired
    //faz uma injeção de dependência
    public EmprestimoRestController(EmprestimoService emprestimoService, LivroService livroService) {
        this.emprestimoService = emprestimoService;
        this.livroService = livroService;
    }
    //indica que esses serviçoes serão injetados aqui no EmprestimoRestController

    @PostMapping("/{livroId}/emprestar")
    public ResponseEntity<String> realizarEmprestimo(@PathVariable Long livroId, @RequestBody Long usuarioId) {
        //@PathVariable diz que vai tirar o livroid da url
    	//@RequestBody pega o id do usuário no body
    	try {
            ResponseEntity<String> response = emprestimoService.realizarEmprestimo(livroId, usuarioId);
            //chama o serviço empréstimo service passando o livroid e usuarioid como parametro
            return response;
            //retorna a respota que o service enviou
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao realizar empréstimo.");
        }
    }

    @PostMapping("/{emprestimoId}/devolver")
    //url
    public ResponseEntity<Void> realizarDevolucao(@PathVariable Long emprestimoId) {
    	//recebe o id do emprestimo da url
        Emprestimo emprestimo = emprestimoService.getEmprestimoById(emprestimoId);
        if (emprestimo == null) {
            return ResponseEntity.notFound().build();
        }
        //busca o emprestimo pelo id e caso não ache nenhum emprestimo naquele id ele retorna um erro

        emprestimoService.realizarDevolucao(emprestimo);
        //é chamado para realizar a devolução
        emprestimoService.atualizarStatusEmprestimos();
        //atualiza o status
        
        Livro livro = emprestimo.getLivro();
        //atribui a variavel livro
        
        livro.setEmprestado(false);
        livroService.editarLivro(livro.getId(), livro);

        return ResponseEntity.ok().build();
        //devolução feita com sucesso
    }
}
