package com.biblioteca.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblioteca.models.Livro;
import com.biblioteca.models.Usuario;
import com.biblioteca.repositories.LivroRepository;

@Service
public class LivroService {

    private final LivroRepository livroRepository;

    @Autowired
    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }
    //njeta uma instanca no livroRepository

    public Livro criarLivro(Livro livro) {
        return livroRepository.save(livro);
    }
    //não faz validações, ele só salva

    public Livro editarLivro(Long id, Livro livroAtualizado) {
        Livro livroExistente = livroRepository.findById(id).orElse(null);
        //procura o livro pelo id no banco de dados e caso não encontre o orElse retorna null

        if (livroExistente != null) {
            livroExistente.setNomeLivro(livroAtualizado.getNomeLivro());
            livroExistente.setEmprestado(livroAtualizado.isEmprestado());

            // Verifica se o livro foi emprestado e atualiza o usuário associado
            if (livroAtualizado.isEmprestado()) {
                Usuario novoUsuario = livroAtualizado.getUsuario();
                livroExistente.setUsuario(novoUsuario);
            } else {}

            return livroRepository.save(livroExistente);
        } else {
            return null;
        }
    }



    public void excluirLivro(Long id) {
        livroRepository.deleteById(id);
    }

    public Livro getLivroById(Long id) {
        return livroRepository.findById(id).orElse(null);
    }
}
