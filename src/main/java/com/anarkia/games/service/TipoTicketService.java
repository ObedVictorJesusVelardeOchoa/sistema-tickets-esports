package com.anarkia.games.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.anarkia.games.dto.TipoTicketDTO;
import com.anarkia.games.modelo.TipoTicket;
import com.anarkia.games.repositorio.DetalleFacturaRepositorio;
import com.anarkia.games.repositorio.TipoTicketRepositorio;

@Service
public class TipoTicketService {

    private final TipoTicketRepositorio tipoTicketRepositorio;
    private final DetalleFacturaRepositorio detalleFacturaRepositorio;

    public TipoTicketService(TipoTicketRepositorio tipoTicketRepositorio, DetalleFacturaRepositorio detalleFacturaRepositorio) {
        this.tipoTicketRepositorio = tipoTicketRepositorio;
        this.detalleFacturaRepositorio = detalleFacturaRepositorio;
    }

    public List<TipoTicketDTO> getTicketsDisponibles() {
        List<TipoTicket> todosLosTipos = tipoTicketRepositorio.findAll();

        return todosLosTipos.stream().map(tipo -> {
            
            long vendidos = detalleFacturaRepositorio.sumCantidadByTipoTicket(tipo.getTipoTicket());
            int stockActual = (int) (tipo.getStockMaximo() - vendidos);

            return new TipoTicketDTO(
                tipo.getTipoTicket(),
                tipo.getEventoNombre(),
                tipo.getDescripcion(),
                tipo.getImagenUrl(),
                tipo.getPrecio(), 
                stockActual
            );
        }).filter(dto -> dto.getStockActual() > 0)
          .collect(Collectors.toList());
    }
}
