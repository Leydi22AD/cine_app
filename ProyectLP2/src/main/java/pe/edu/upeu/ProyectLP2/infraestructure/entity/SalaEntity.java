package pe.edu.upeu.ProyectLP2.infraestructure.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sala")
public class SalaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSala;

    @Column(nullable = false)
    private Integer numero;

    @Column(nullable = false)
    private Integer filas;

    @Column(nullable = false)
    private Integer columnas;

    @OneToMany(mappedBy = "sala", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AsientoEntity> asientos = new ArrayList<>();

    public SalaEntity() {
    }

    public SalaEntity(Long idSala, Integer filas, Integer numero, Integer columnas, List<AsientoEntity> asientos) {
        this.idSala = idSala;
        this.filas = filas;
        this.numero = numero;
        this.columnas = columnas;
        this.asientos = asientos;
    }

    public Long getIdSala() {
        return idSala;
    }

    public void setIdSala(Long idSala) {
        this.idSala = idSala;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getFilas() {
        return filas;
    }

    public void setFilas(Integer filas) {
        this.filas = filas;
    }

    public List<AsientoEntity> getAsientos() {
        return asientos;
    }

    public void setAsientos(List<AsientoEntity> asientos) {
        this.asientos = asientos;
    }

    public Integer getColumnas() {
        return columnas;
    }

    public void setColumnas(Integer columnas) {
        this.columnas = columnas;
    }
}
