package com.biblioteca.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.biblioteca.models.Emprestimo;
import com.biblioteca.models.Livro;
import com.biblioteca.models.Usuario;
import com.biblioteca.repositories.EmprestimoRepository;

@Service
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final LivroService livroService;
    private final UsuarioService usuarioService;

    @Autowired
    public EmprestimoService(EmprestimoRepository emprestimoRepository, LivroService livroService, UsuarioService usuarioService) {
        this.emprestimoRepository = emprestimoRepository;
        this.livroService = livroService;
        this.usuarioService = usuarioService;
    }
    //injeta a dependencia ao emprestimoRepository atraves do @Autowired

    public ResponseEntity<String> realizarEmprestimo(Long livroId, Long usuarioId) {
        Livro livro = livroService.getLivroById(livroId);
        Usuario usuario = usuarioService.getUsuarioById(usuarioId);
      //obtem o livro e o usuario

        if (livro == null || usuario == null) {
            return ResponseEntity.notFound().build();
        }
        //verifica se o livro e o usuario são nulos
        //caso sejam restorna um erro 404

        if (livro.isEmprestado()) {
            return ResponseEntity.badRequest().body("Livro indisponível para empréstimo.");
            //ele retorna um erro 400 com essa mensagem
        }
        //verfica se emprestado está true, atravez do isEmprestado, caso esteja  emprestado ele retorna uma mensagem

        int maximoEmprestimos = 3; // limite máximo de empréstimos
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
        emprestimoRepository.save(emprestimo);
        //chama o emprestimoService para realizar o emprestimo

        livro.setEmprestado(true);
        //muda o estado de emprestado
        livroService.editarLivro(livroId, livro);
        //nesta frase ele realmente atualiza o estado do emprestado e ele adiciona mais 1 na quantidade de emprestimo
        

        String mensagem = "Empréstimo realizado com sucesso!\n"
                + "Data do empréstimo: " + dataEmprestimo + ";\n"
                + "Prazo de devolução: " + dataPrazoEntrega + ".";
        return ResponseEntity.status(HttpStatus.CREATED).body(mensagem);
      //restorna a mensagem criada 
    }

  
    
    public void atualizarStatusEmprestimos() {
        List<Emprestimo> emprestimos = emprestimoRepository.findAll();
        //busca todos os emprestimos do banco de dados usando o metodo finAll e guarda tudo na  lista emprestimos
        LocalDate dataAtual = LocalDate.now();
        //armazena a data atual
        
        for (Emprestimo emprestimo : emprestimos) {
            if (emprestimo.getStatus().equals("ativo")) {
            	
                // Verificar se está em atraso
                if (emprestimo.getDataPrazoEntrega().isBefore(dataAtual)) {
                	//isBefore verifica se uma data é anterior a outra data
                	//verifica se a dataPrazoEntrega é anteror a data atual
                    emprestimo.setStatus("em atraso");
                    emprestimoRepository.save(emprestimo);
                    
                    
                    // Verificar se já foi entregue
                } else if (emprestimo.getDataDevolucao() != null) {
                    if (emprestimo.getDataDevolucao().isBefore(emprestimo.getDataPrazoEntrega())) {
                        emprestimo.setStatus("entregue no prazo");
                    } else {
                        emprestimo.setStatus("entregue em atraso");
                    }
                    emprestimoRepository.save(emprestimo);
                }
            }
        }
    }
    
    @Scheduled(cron = "0 * * * *")
    //executa a tareda de meia noite todos os dias
    
    //Scheduled é usada para agendar uma tarefa para ser feita em um horario marcado
    //5 0 * * * - execute a tarefa às 5h da manhã todos os dias
    public void atualizarStatusEmprestimosAgendado() {
        atualizarStatusEmprestimos();
    }
   



    public Emprestimo realizarDevolucao(Emprestimo emprestimo) {
    	emprestimo.setDataDevolucao(LocalDate.now());
        return emprestimoRepository.save(emprestimo);
    }
 
    /*
    public void excluirEmprestimo(Long id) {
        emprestimoRepository.deleteById(id);
    }
    */

    public Emprestimo getEmprestimoById(Long id) {
        return emprestimoRepository.findById(id).orElse(null);
    }
}
