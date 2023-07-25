package com.biblioteca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca.models.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

}
