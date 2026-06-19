package pe.edu.upeu.ProyectLP2.domain.model;

import pe.edu.upeu.ProyectLP2.infraestructure.entity.Rol;

public class Usuario {
    private Long idUsuario;
    private String nombre;
    private String email;
    private String password;
    private Rol rol;

    public Usuario() {
    }

    public Usuario(Long idUsuario, String nombre, String email, String password, Rol rol) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    // Alias methods for backward compatibility
    public String getCorreo() {
        return email;
    }

    public void setCorreo(String correo) {
        this.email = correo;
    }
}
