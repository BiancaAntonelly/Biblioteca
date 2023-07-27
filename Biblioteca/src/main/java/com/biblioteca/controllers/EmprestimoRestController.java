package com.biblioteca.controllers;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.models.Emprestimo;
import com.biblioteca.models.Livro;
import com.biblioteca.models.Usuario;
import com.biblioteca.services.EmprestimoService;
import com.biblioteca.services.LivroService;
import com.biblioteca.services.UsuarioService;

@RestController
//indica que a classe é um controlador rest
@RequestMapping("/emprestimos")
//mapeia as url
public class EmprestimoRestController {

	
    private final EmprestimoService emprestimoService;
    private final LivroService livroService;
    private final UsuarioService usuarioService;

    @Autowired
    public EmprestimoRestController(EmprestimoService emprestimoService, LivroService livroService, UsuarioService usuarioService) {
        this.emprestimoService = emprestimoService;
        this.livroService = livroService;
        this.usuarioService = usuarioService;
    }
    //indica que esses serviçoes serão injetados aqui no EmprestimoRestController

    @PostMapping("/{livroId}/emprestar")
    public ResponseEntity<String> realizarEmprestimo(@PathVariable Long livroId, @RequestBody Long usuarioId) {
        //recebe como parametro o livro id da url do pathvariabe e o usuario id
    	try {
            Livro livro = livroService.getLivroById(livroId);
            Usuario usuario = usuarioService.getUsuarioById(usuarioId);
            //obtem o livro e o usuario

            if (livro == null || usuario == null) {
                return ResponseEntity.notFound().build();
            }
            //verifica se o livro e o usuario ão nulos
            //caso sejam restorna um erro 404

            if (livro.isEmprestado()) {
                return ResponseEntity.badRequest().body("Livro indisponível para empréstimo.");
            }
            //verfica se emprestado está true, atravez do isEmprestado, caso esteja  emprestado ele retorna uma mensagem
            
            int maximoEmprestimos = 3; //limite máximo de empréstimos
            if (livro.getQuantidadeEmprestimos() >= maximoEmprestimos) {
                return ResponseEntity.badRequest().body("Limite de empréstimos atingido para este livro.");
            }
            //usa o getQuantidadeEmprestimos para ver quantas vezes o livro fo emprestado

            
            // Realizar o empréstimo
            LocalDate dataEmprestimo = LocalDate.now();
            //guarda a data atual
            LocalDate dataPrazoEntrega = dataEmprestimo.plusDays(5); // Prazo de 30 dias para entrega
            //calcula mais 5 dias e faz a data de entrega
            LocalDate dataDevolucao = null; 
            // Ainda não foi devolvido
            String status = "ativo";
            // Status inicial é "ativo"
            
            Emprestimo emprestimo = new Emprestimo(null, livro, usuario, dataEmprestimo, dataPrazoEntrega, dataDevolucao, status);
            //cria um novo objeto emprestimo com os dados
            emprestimoService.realizarEmprestimo(emprestimo);
            //chama o emprestimoService para realizar o emprestimo
            
            
            livro.setEmprestado(true);
            //muda o estado de emprestado
            livroService.editarLivro(livroId, livro);
            //nesta frase ele realmente atualiza o etsado do emprestado e ele adiciona mais 1 na quantidade de emprestimo
            
            
            String mensagem = "Empréstimo realizado com sucesso!\n"
                    + "Data do empréstimo: " + dataEmprestimo + ";\n"
                    + "Prazo de devolução: " + dataPrazoEntrega + ".";
            return ResponseEntity.status(HttpStatus.CREATED).body(mensagem);
            //restorna a mensagem criada 
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao realizar empréstimo.");
            //caso tenha dado algo de errado 
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
        //busca o emprestimo pelo d e caso não ache nenhum emprestimo naquele id ele retorna um erro

        emprestimoService.realizarDevolucao(emprestimo);
        //é chamado para realizar a devolução
        emprestimoService.atualizarStatusEmprestimos();
        //atualiza o status
        
        Livro livro = emprestimo.getLivro();
        livro.setEmprestado(false);
        
        livroService.editarLivro(livro.getId(), livro);

        return ResponseEntity.ok().build();
        //devolução feita com sucesso
    }
}
