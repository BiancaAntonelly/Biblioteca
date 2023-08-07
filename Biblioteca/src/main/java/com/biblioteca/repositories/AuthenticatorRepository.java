package com.biblioteca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.biblioteca.models.Auth;

import java.util.Optional;

@Repository
public interface AuthenticatorRepository extends JpaRepository<Auth, Long> {
    Optional<Auth> findByUsername(String username);
}
