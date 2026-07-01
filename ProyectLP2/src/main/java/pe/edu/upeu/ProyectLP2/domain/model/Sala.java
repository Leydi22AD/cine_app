package pe.edu.upeu.ProyectLP2.domain.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;

public class Sala {

    private Long idSala;
    private Integer numero;
    private Integer filas;
    private Integer columnas;
    
    @JsonManagedReference
    private List<Asiento> asientos;

    public Sala() {
    }

    public Sala(Long idSala, Integer numero, Integer filas, Integer columnas, List<Asiento> asientos) {
        this.idSala = idSala;
        this.numero = numero;
        this.filas = filas;
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

    public Integer getColumnas() {
        return columnas;
    }

    public void setColumnas(Integer columnas) {
        this.columnas = columnas;
    }

    public List<Asiento> getAsientos() {
        return asientos;
    }

    public void setAsientos(List<Asiento> asientos) {
        this.asientos = asientos;
    }
}