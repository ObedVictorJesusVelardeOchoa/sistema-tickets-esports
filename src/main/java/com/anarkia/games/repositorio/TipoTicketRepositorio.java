package com.anarkia.games.repositorio;
import org.springframework.data.jpa.repository.JpaRepository;

import com.anarkia.games.modelo.TipoTicket;

public interface  TipoTicketRepositorio extends JpaRepository<TipoTicket, String> {
}