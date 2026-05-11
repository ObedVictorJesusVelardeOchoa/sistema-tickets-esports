package com.anarkia.games.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.Filter;

@RestController
public class SecurityDebugController {

    @Autowired
    private List<SecurityFilterChain> filterChains;

    @GetMapping("/debug-security")
    public String getSecurityConfig() {
        StringBuilder sb = new StringBuilder();
        sb.append("<h1>Configuracion de Seguridad Activa:</h1>");

        sb.append("<h2>Se encontraron ").append(filterChains.size()).append(" cadenas de filtros.</h2><hr>");
        
        for (int i = 0; i < filterChains.size(); i++) {
            SecurityFilterChain chain = filterChains.get(i);
            sb.append("<h3>Cadena de Filtros #").append(i + 1).append("</h3>");
            sb.append("<p><b>Filtros en esta cadena (en orden):</b></p>");
            sb.append("<ol>");

            for (Filter filter : chain.getFilters()) {
                sb.append("<li>").append(filter.getClass().getName()).append("</li>");
            }
            sb.append("</ol><hr>");
        }
        
        return sb.toString();
    }
}