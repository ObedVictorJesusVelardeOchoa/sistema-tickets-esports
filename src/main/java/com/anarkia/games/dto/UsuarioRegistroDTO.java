package com.anarkia.games.dto;

public class UsuarioRegistroDTO {
    private String nombre;
    private String apellidos;
    private String email;
    private String password;
    private String tipoCliente; 

    public UsuarioRegistroDTO() {}

    public UsuarioRegistroDTO(String nombre, String apellidos, String email, String password, String tipoCliente) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.password = password;
        this.tipoCliente = tipoCliente;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getTipoCliente() { return tipoCliente; }
    public void setTipoCliente(String tipoCliente) { this.tipoCliente = tipoCliente; }
}