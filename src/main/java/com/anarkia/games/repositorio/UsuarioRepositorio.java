package com.anarkia.games.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anarkia.games.modelo.Usuario;

@Repository
public interface  UsuarioRepositorio extends JpaRepository<Usuario, Long>{
    
    public Usuario findByCorreo(String correo);

    List<Usuario> findByRol(String rol);

    Usuario findByResetPasswordToken(String token);
}
