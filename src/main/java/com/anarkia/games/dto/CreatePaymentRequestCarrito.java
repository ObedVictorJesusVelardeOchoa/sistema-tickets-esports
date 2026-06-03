package com.anarkia.games.dto;

import java.util.List;

public class CreatePaymentRequestCarrito {
    private List<CartItem> items;

    // Getters y Setters
    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }
}