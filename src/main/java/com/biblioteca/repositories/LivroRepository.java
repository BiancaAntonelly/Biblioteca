package com.biblioteca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca.models.Livro;

public interface LivroRepository extends JpaRepository<Livro, Long>{

}
