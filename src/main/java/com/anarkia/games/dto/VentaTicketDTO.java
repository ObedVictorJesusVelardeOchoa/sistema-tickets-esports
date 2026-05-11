package com.anarkia.games.dto;

public class VentaTicketDTO {

    private String nombreCliente;
    private String tipoTicket;
    private String eventoNombre; 
    private Long totalComprado;

    public VentaTicketDTO(String nombreCliente, String tipoTicket, String eventoNombre, Long totalComprado) {
        this.nombreCliente = nombreCliente;
        this.tipoTicket = tipoTicket;
        this.eventoNombre = eventoNombre; 
        this.totalComprado = totalComprado;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getTipoTicket() {
        return tipoTicket;
    }

    public String getEventoNombre() {
        return eventoNombre;
    }

    public Long getTotalComprado() {
        return totalComprado;
    }
}