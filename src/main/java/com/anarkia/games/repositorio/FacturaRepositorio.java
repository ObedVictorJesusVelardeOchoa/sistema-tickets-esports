package com.anarkia.games.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anarkia.games.modelo.Factura;

@Repository
public interface FacturaRepositorio extends JpaRepository<Factura, Long> {
}