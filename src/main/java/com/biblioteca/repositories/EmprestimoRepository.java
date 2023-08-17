package com.biblioteca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca.models.Emprestimo;


public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long>{

}
