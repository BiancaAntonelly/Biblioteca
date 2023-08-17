package com.biblioteca.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca.models.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	 Optional<Usuario> findByUsername(String username);
}
