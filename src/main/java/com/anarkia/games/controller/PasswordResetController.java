package com.anarkia.games.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anarkia.games.service.UsuarioService;

@Controller
public class PasswordResetController {

    private final UsuarioService usuarioService;

    public PasswordResetController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }


    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.generatePasswordResetToken(email);
            redirectAttributes.addFlashAttribute("message", "Se ha enviado un código de recuperación a tu correo.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/forgot-password";
        }
        return "redirect:/reset-password"; 
    }

    @GetMapping("/reset-password")
public String showResetPasswordForm() {
    return "reset-password";
}


@PostMapping("/reset-password")
public String processResetPassword(@RequestParam("token") String token,
                                   @RequestParam("password") String password,
                                   RedirectAttributes redirectAttributes) {
    try {
        usuarioService.resetPassword(token, password);
        redirectAttributes.addFlashAttribute("message", "Tu contraseña ha sido cambiada exitosamente.");
    } catch (RuntimeException e) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/reset-password";
    }
    return "redirect:/InicioSesion";
}
}