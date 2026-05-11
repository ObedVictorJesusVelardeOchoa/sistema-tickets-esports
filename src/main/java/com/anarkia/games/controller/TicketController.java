package com.anarkia.games.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam; 

import com.anarkia.games.dto.TipoTicketDTO;
import com.anarkia.games.service.TipoTicketService;

@Controller
public class TicketController {

    private final TipoTicketService tipoTicketService;

    public TicketController(TipoTicketService tipoTicketService) {
        this.tipoTicketService = tipoTicketService;
    }

    @GetMapping("/Tickets") 
    public String mostrarInfoGeneral() {
        return "Tickets";
    }

    @GetMapping("/TicketsII")
    public String mostrarSeleccionDeTipo() {
        return "Tickets-II";
    }

    @GetMapping("/TicketsIII")
    public String mostrarTiendaFiltrada(@RequestParam("categoria") String categoria, Model model) {
        
        List<TipoTicketDTO> todosLosTickets = tipoTicketService.getTicketsDisponibles();
        List<TipoTicketDTO> ticketsFiltrados = todosLosTickets.stream()
                .filter(ticket -> ticket.getTipo().startsWith(categoria))
                .collect(Collectors.toList());
        model.addAttribute("tickets", ticketsFiltrados);
        model.addAttribute("categoriaSeleccionada", categoria);
        return "Tickets-III"; 
    }

    @GetMapping("/pago-exitoso")
        public String mostrarPaginaDeExito() {
        
    return "pago-exitoso";
}
}