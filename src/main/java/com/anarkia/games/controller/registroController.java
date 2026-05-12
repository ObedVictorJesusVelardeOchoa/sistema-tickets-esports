package com.anarkia.games.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.anarkia.games.dto.VentaTicketDTO;

import com.anarkia.games.modelo.Usuario;
import com.anarkia.games.repositorio.DetalleFacturaRepositorio;

import com.anarkia.games.service.UsuarioService;

@Controller
public class registroController {

    private final UsuarioService usuarioService;

    private final DetalleFacturaRepositorio detalleFacturaRepositorio;

    public registroController(UsuarioService usuarioService, 
            DetalleFacturaRepositorio detalleFacturaRepositorio) {
        this.usuarioService = usuarioService;

        this.detalleFacturaRepositorio = detalleFacturaRepositorio;
    }

    @GetMapping("/InicioSesion")
    public String iniciarSesion() {
        return "Login";
    }

    @GetMapping("/AdminRegistro")
    public String mostrarAdminRegistro(Model model) {

        List<Usuario> clientes = usuarioService.obtenerUsuariosPorRol("Cliente");

        
        List<VentaTicketDTO> todasLasVentas = detalleFacturaRepositorio.findTotalTicketsCompradosPorCliente();

       
        List<VentaTicketDTO> ventasCompetidores = todasLasVentas.stream()
                .filter(venta -> venta.getTipoTicket().toLowerCase().contains("competidor"))
                .collect(Collectors.toList());

        List<VentaTicketDTO> ventasEspectadores = todasLasVentas.stream()
                .filter(venta -> venta.getTipoTicket().toLowerCase().contains("espectador"))
                .collect(Collectors.toList());

        long totalCompetidores = ventasCompetidores.stream()
                .mapToLong(VentaTicketDTO::getTotalComprado)
                .sum();

        long totalEspectadores = ventasEspectadores.stream()
                .mapToLong(VentaTicketDTO::getTotalComprado)
                .sum();

        model.addAttribute("clientes", clientes);
        model.addAttribute("ventasCompetidores", ventasCompetidores);
        model.addAttribute("ventasEspectadores", ventasEspectadores);

        model.addAttribute("totalCompetidores", totalCompetidores);
        model.addAttribute("totalEspectadores", totalEspectadores);


        Map<String, Map<String, Long>> datosPorTorneo = new HashMap<>();

        for (VentaTicketDTO venta : todasLasVentas) {

            String torneo = venta.getEventoNombre();

    
            if (torneo == null || torneo.trim().isEmpty()) {
                torneo = "Torneo Desconocido";
            }

            String tipoTicket = venta.getTipoTicket().toLowerCase();

            String categoria = tipoTicket.contains("competidor") ? "Competidor" : "Espectador";


            datosPorTorneo.putIfAbsent(torneo, new HashMap<>());
            datosPorTorneo.get(torneo).merge(categoria, venta.getTotalComprado(), Long::sum);
        }

        model.addAttribute("datosPorTorneo", datosPorTorneo);

        return "AdminRegistro";

    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable("id") Long id) {
        usuarioService.eliminarUsuario(id);
        return "redirect:/AdminRegistro?eliminado";
    }
}