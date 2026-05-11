package com.anarkia.games.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.anarkia.games.modelo.Usuario;
import com.anarkia.games.repositorio.UsuarioRepositorio;
import com.anarkia.games.service.UsuarioService;

@Controller
public class ClienteController {

    private final UsuarioRepositorio usuarioRepositorio;
    private final UsuarioService usuarioService;

    public ClienteController(UsuarioRepositorio usuarioRepositorio, UsuarioService usuarioService) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/Perfil")
    public String mostrarPerfil(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String correo = authentication.getName();
        
        Usuario usuario = usuarioRepositorio.findByCorreo(correo);

        model.addAttribute("usuario", usuario);

        return "Perfil";
    }

    @PostMapping("/perfil/actualizar-nombre")
    public String actualizarNombre(@RequestParam String nuevoNombre) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String correo = authentication.getName();
        
        usuarioService.actualizarNombre(correo, nuevoNombre);
        
        return "redirect:/Perfil?exito";
    }
}