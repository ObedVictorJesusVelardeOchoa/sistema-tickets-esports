package com.anarkia.games.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anarkia.games.modelo.Administrador;

@Repository
public interface AdministradorRepositorio extends JpaRepository<Administrador, Long> {
}