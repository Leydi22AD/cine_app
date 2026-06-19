package pe.edu.upeu.ProyectLP2.infraestructure.adapter.controller.dto;

import pe.edu.upeu.ProyectLP2.infraestructure.entity.Rol;

import javax.management.relation.Role;

// Definimos un record principal llamado UsuarioDto (puede servir como agrupador de los sub-records).
public record UsuarioDto() {


    // Sub-record que representa el DTO de entrada para crear/registrar un usuario.
    // Este es el objeto que el cliente enviará en la petición (ejemplo: POST /usuarios).
    public record UsuarioRequest(
            String nombre,
            String email,
            String password,
            Rol rol
    ) {}

    // Sub-record que representa el DTO de salida, es decir,
    // lo que el sistema devuelve como respuesta al cliente.
    public record UsuarioResponse(
            Long id,
            String nombre,
            String email,
            Rol rol
    ) {}


}
