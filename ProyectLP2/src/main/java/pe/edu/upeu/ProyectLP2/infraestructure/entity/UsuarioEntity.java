package pe.edu.upeu.ProyectLP2.infraestructure.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "usuario")
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    // Nombre completo del usuario (puede ser nulo, máximo 100 caracteres)
    @Column(nullable = false, length = 100)
    private String nombre;

    // Email obligatorio, único y de máximo 50 caracteres
    @Column(nullable = false, length = 50, unique = true) /*email puede ser nulo, con ancho 100 y unico*/
    private String email;

    // Contraseña obligatoria de máximo 100 caracteres
    @Column(nullable = false, length = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    public UsuarioEntity() {
    }

    public UsuarioEntity(Long idUsuario, String email, String nombre, String password, Rol rol) {
        this.idUsuario = idUsuario;
        this.email = email;
        this.nombre = nombre;
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
}
