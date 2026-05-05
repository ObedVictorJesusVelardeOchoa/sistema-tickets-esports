package com.anarkia.games.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tipo_ticket")
public class TipoTicket {

    @Id
    @Column(name = "tipo_ticket")
    private String tipoTicket;

    @Column(name = "stock_maximo", nullable = false)
    private int stockMaximo;

    @Column(name = "precio", nullable = false)
    private long precio;

    @Column(name = "evento_nombre", nullable = false)
    private String eventoNombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "imagen_url")
    private String imagenUrl;

    public TipoTicket() {}

    // --- Getters y Setters ---
    public String getTipoTicket() {
        return tipoTicket;
    }

    public void setTipoTicket(String tipoTicket) {
        this.tipoTicket = tipoTicket;
    }

    public int getStockMaximo() {
        return stockMaximo;
    }

    public void setStockMaximo(int stockMaximo) {
        this.stockMaximo = stockMaximo;
    }

    public long getPrecio() {
        return precio;
    }

    public void setPrecio(long precio) {
        this.precio = precio;
    }

    public String getEventoNombre() {
        return eventoNombre;
    }

    public void setEventoNombre(String eventoNombre) {
        this.eventoNombre = eventoNombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}