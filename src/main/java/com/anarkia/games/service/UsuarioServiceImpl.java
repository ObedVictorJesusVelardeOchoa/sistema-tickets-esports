package com.anarkia.games.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.anarkia.games.dto.UsuarioRegistroDTO;
import com.anarkia.games.modelo.Cliente;
import com.anarkia.games.modelo.Usuario;
import com.anarkia.games.repositorio.UsuarioRepositorio;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepositorio usuarioRepositorio;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    public UsuarioServiceImpl(UsuarioRepositorio usuarioRepositorio, 
                              BCryptPasswordEncoder passwordEncoder, 
                              JavaMailSender mailSender) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }


    @Override
public Usuario guardar(UsuarioRegistroDTO registroDTO) {
    Cliente cliente = new Cliente();

    cliente.setNombre(registroDTO.getNombre());
    cliente.setCorreo(registroDTO.getEmail());
    cliente.setClave(passwordEncoder.encode(registroDTO.getPassword()));
    cliente.setFecha(new Date());
    cliente.setRol("Cliente");

    String tipoCliente = registroDTO.getTipoCliente();


    if (tipoCliente == null || tipoCliente.isEmpty()) {
        tipoCliente = "Espectador";
    }


    cliente.setTipoCliente(tipoCliente);

    return usuarioRepositorio.save(cliente);
}

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.findByCorreo(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario o contraseña inválidos");
        }

        return User.builder()
                .username(usuario.getCorreo())
                .password(usuario.getClave())
                .authorities(usuario.getRol())
                .build();
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepositorio.findAll();
    }

    @Override
    public void eliminarUsuario(Long id) {
        usuarioRepositorio.deleteById(id);
    }

    @Override
    public List<Usuario> obtenerUsuariosPorRol(String rol) {
        return usuarioRepositorio.findByRol(rol);
    }

    @Override
    public void actualizarNombre(String correo, String nuevoNombre) {
        // 1. Busca al usuario en la base de datos por su correo
        Usuario usuario = usuarioRepositorio.findByCorreo(correo);
        

        if (usuario != null && nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {

            usuario.setNombre(nuevoNombre);
           
            usuarioRepositorio.save(usuario);
        }
    }

    @Override
    public void generatePasswordResetToken(String email) {
        Usuario usuario = usuarioRepositorio.findByCorreo(email);
        if (usuario == null) {
            throw new RuntimeException("No se encontró un usuario con ese correo electrónico.");
        }


        String token = String.format("%06d", new Random().nextInt(999999));
        

        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(15);

        usuario.setResetPasswordToken(token);
        usuario.setTokenExpiryDate(expiryDate);
        
        usuarioRepositorio.save(usuario);


        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(usuario.getCorreo());
        message.setSubject("Código de Recuperación de Contraseña");
        message.setText("Tu código de recuperación es: " + token + 
                        "\nEste código expirará en 15 minutos.");
        mailSender.send(message);
    }

    @Override
public void resetPassword(String token, String newPassword) {

    Usuario usuario = usuarioRepositorio.findByResetPasswordToken(token);


    if (usuario == null || usuario.getTokenExpiryDate().isBefore(LocalDateTime.now())) {
        throw new RuntimeException("El token es inválido o ha expirado.");
    }


    usuario.setClave(passwordEncoder.encode(newPassword));
    
    usuario.setResetPasswordToken(null);
    usuario.setTokenExpiryDate(null);
    
    usuarioRepositorio.save(usuario);
}
}
