package com.biblioteca.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.biblioteca.models.Emprestimo;
import com.biblioteca.models.Livro;
import com.biblioteca.repositories.EmprestimoRepository;

@Service
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;

    @Autowired
    public EmprestimoService(EmprestimoRepository emprestimoRepository) {
        this.emprestimoRepository = emprestimoRepository;
    }
    //injeta a dependencia ao emprestimoRepository atravez do @Autowired

    public Emprestimo realizarEmprestimo(Emprestimo emprestimo) {
    	/*Livro livro = emprestimo.getLivro();
        if (livro == null) {
            throw new IllegalArgumentException("O empréstimo não pode ser realizado pois o livro não foi informado.");
        }
        if (livro.isEmprestado()) {
            throw new IllegalArgumentException("O empréstimo não pode ser realizado pois o livro está indisponível para empréstimo.");
        }
        
        int maximoEmprestimos = 3; // Limite máximo de empréstimos
        if (livro.getQuantidadeEmprestimos() >= maximoEmprestimos) {
            throw new IllegalArgumentException("O empréstimo não pode ser realizado pois o livro atingiu o limite máximo de empréstimos permitidos.");
        }
        //usa o getQuantidadeEmprestimos para ver quantas vezes o livro fo emprestado
        */
        return emprestimoRepository.save(emprestimo);
    }
    
    public void atualizarStatusEmprestimos() {
        List<Emprestimo> emprestimos = emprestimoRepository.findAll();
        //pega todos os emprestimos         
        LocalDate dataAtual = LocalDate.now();
        //armazena a data atual
        
        for (Emprestimo emprestimo : emprestimos) {
            if (emprestimo.getStatus().equals("ativo")) {
            	
                // Verificar se está em atraso
                if (emprestimo.getDataPrazoEntrega().isBefore(dataAtual)) {
                	//isBefore verifica se uma data é anterior  aoutra data
                	//verifica se a dataPrazoEntrega pe anteror a data atual
                    emprestimo.setStatus("atraso");
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
    
    @Scheduled(cron = "0 0 * * *")
    //Scheduled é usada para agendar uma tarefa para ser feita em um horario marcado
    //5 0 * * * - execute a tarefa às 5h da manhã todos os dias
    public void atualizarStatusEmprestimosAgendado() {
        atualizarStatusEmprestimos();
    }

    public Emprestimo realizarDevolucao(Emprestimo emprestimo) {
    	emprestimo.setDataDevolucao(LocalDate.now());
        return emprestimoRepository.save(emprestimo);
    }
 
    public void excluirEmprestimo(Long id) {
        emprestimoRepository.deleteById(id);
    }

    public Emprestimo getEmprestimoById(Long id) {
        return emprestimoRepository.findById(id).orElse(null);
    }
}
