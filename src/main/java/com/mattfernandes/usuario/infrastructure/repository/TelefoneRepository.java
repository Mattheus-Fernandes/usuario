package com.mattfernandes.usuario.infrastructure.repository;

import com.mattfernandes.usuario.infrastructure.entity.Telefone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelefoneRepository extends JpaRepository<Telefone, Long> {
}