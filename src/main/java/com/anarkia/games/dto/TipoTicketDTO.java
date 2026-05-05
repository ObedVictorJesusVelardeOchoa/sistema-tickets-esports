package com.anarkia.games.dto;

public class TipoTicketDTO {

    private String tipo;
    private String eventoNombre;
    private String descripcion;
    private String imagenUrl;
    private long precio; // Precio en centavos
    private int stockActual;


    public TipoTicketDTO(String tipo, String eventoNombre, String descripcion, String imagenUrl, long precio, int stockActual) {
        this.tipo = tipo;
        this.eventoNombre = eventoNombre;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
        this.precio = precio;
        this.stockActual = stockActual;
    }

   
    public String getTipo() {
        return tipo;
    }

    public String getEventoNombre() {
        return eventoNombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public long getPrecio() {
        return precio;
    }

    public int getStockActual() {
        return stockActual;
    }
}