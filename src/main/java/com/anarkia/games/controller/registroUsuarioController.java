package com.anarkia.games.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.anarkia.games.dto.UsuarioRegistroDTO;
import com.anarkia.games.service.UsuarioService;

@Controller
@RequestMapping("/Registrarse")
public class registroUsuarioController {
    private static final Logger logger = LoggerFactory.getLogger(registroUsuarioController.class);
    
    private final UsuarioService usuarioService;
    @Autowired
    private JavaMailSender mailSender;

    public registroUsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @ModelAttribute("usuario")
    public UsuarioRegistroDTO retornarNuevoUsuarioRegistroDTO() {
        return new UsuarioRegistroDTO();
    }

    @GetMapping
    public String mostrarFormularioDeRegistro() {
        return "Registrarse";
    }

    @PostMapping
    public String registrarCuentaDeUsuario(@ModelAttribute("usuario") UsuarioRegistroDTO registroDTO){
        logger.info("--- INTENTO DE REGISTRO RECIBIDO ---");
        if (registroDTO != null) {
            logger.info("Datos del formulario: Nombre='{}', Email='{}'", registroDTO.getNombre(), registroDTO.getEmail());
        } else {
            logger.error("¡ALERTA! El objeto registroDTO es nulo. El formulario podría no estar bien configurado.");
            return "redirect:/Registrarse?error";
        }

        try {
            usuarioService.guardar(registroDTO);
            logger.info("✅ Llamada al servicio 'guardar' completada exitosamente.");
        } catch (Exception e) {
            logger.error("❌ ERROR al intentar guardar el usuario en el servicio:", e);
            return "redirect:/Registrarse?error";
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(registroDTO.getEmail());
            message.setSubject("Bienvenido a Anarkia Games 🎮");
            message.setText("Hola " + registroDTO.getNombre() + ", tu cuenta ha sido registrada exitosamente.");
            mailSender.send(message);
            logger.info("✅ Correo de bienvenida enviado a {}.", registroDTO.getEmail());
        } catch (Exception e) {
            logger.error("❌ ERROR al enviar el correo de bienvenida:", e);
        }
        
        logger.info("--- PROCESO DE REGISTRO FINALIZADO ---");
        return "redirect:/Registrarse?exito";
    }
}