package com.anarkia.games.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anarkia.games.modelo.Administrador;
import com.anarkia.games.repositorio.AdministradorRepositorio;

@Service
public class AdministradorService {

    @Autowired
    private AdministradorRepositorio administradorRepositorio;

    public List<Administrador> listarAdministradores() {
        return administradorRepositorio.findAll();
    }

    public void eliminarAdministrador(Long id) {
        administradorRepositorio.deleteById(id);
    }
}