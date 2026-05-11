package com.anarkia.games.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.anarkia.games.dto.UsuarioRegistroDTO;
import com.anarkia.games.modelo.Usuario;

public interface UsuarioService extends UserDetailsService {
    
    public Usuario guardar(UsuarioRegistroDTO registroDTO);

    public List<Usuario> listarUsuarios();

    public void eliminarUsuario(Long id);

    List<Usuario> obtenerUsuariosPorRol(String rol);
    
    public void actualizarNombre(String correo, String nuevoNombre);
    
    public void generatePasswordResetToken(String email);

    public void resetPassword(String token, String newPassword);
}
