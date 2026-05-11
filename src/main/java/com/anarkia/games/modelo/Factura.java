package com.anarkia.games.modelo;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "factura")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_fac")
    private Long codFac;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Cliente cliente;

    @Column(name = "fac_emi", nullable = false)
    private LocalDateTime fechaEmision;

    @Column(name = "total_pagado", nullable = false)
    private long totalPagado;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL)
    private List<DetalleFactura> detalles;

    public Long getCodFac() {
        return codFac;
    }
    public void setCodFac(Long codFac) {
        this.codFac = codFac;
    }
    public Cliente getCliente() {
        return cliente;
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }
    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }
    public long getTotalPagado() {
        return totalPagado;
    }
    public void setTotalPagado(long totalPagado) {
        this.totalPagado = totalPagado;
    }
    public List<DetalleFactura> getDetalles() {
        return detalles;
    }
    public void setDetalles(List<DetalleFactura> detalles) {
        this.detalles = detalles;
    }
}