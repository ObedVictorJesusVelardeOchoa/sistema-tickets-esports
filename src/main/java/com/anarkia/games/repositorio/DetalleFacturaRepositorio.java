package com.anarkia.games.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.anarkia.games.dto.VentaTicketDTO;
import com.anarkia.games.modelo.DetalleFactura;

@Repository
public interface DetalleFacturaRepositorio extends JpaRepository<DetalleFactura, Long> {

    @Query("SELECT COALESCE(SUM(df.cantidad), 0) FROM DetalleFactura df WHERE df.tipoTicket.tipoTicket = :tipoTicket")
    long sumCantidadByTipoTicket(@Param("tipoTicket") String tipoTicket);


    @Query("SELECT new com.anarkia.games.dto.VentaTicketDTO(" +
            "f.cliente.nombre, " +
            "df.tipoTicket.tipoTicket, " + 
            "df.tipoTicket.eventoNombre, " +
            "SUM(df.cantidad)) " +
            "FROM DetalleFactura df JOIN df.factura f " +
            "GROUP BY f.cliente.nombre, df.tipoTicket.tipoTicket, df.tipoTicket.eventoNombre")
    List<VentaTicketDTO> findTotalTicketsCompradosPorCliente();

}