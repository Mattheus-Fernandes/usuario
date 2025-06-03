package com.mattfernandes.usuario.infrastructure.repository;


import com.mattfernandes.usuario.infrastructure.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}