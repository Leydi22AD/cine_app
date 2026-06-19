package pe.edu.upeu.ProyectLP2.domain.model;

public class Asiento {
    private Long idAsiento;
    private Integer fila;
    private Integer columna;
    private String estado;
    private Sala sala;

    public Asiento() {
    }

    public Asiento(Long idAsiento, Integer fila, Integer columna, String estado, Sala sala) {
        this.idAsiento = idAsiento;
        this.fila = fila;
        this.columna = columna;
        this.estado = estado;
        this.sala = sala;
    }

    public Long getIdAsiento() {
        return idAsiento;
    }

    public void setIdAsiento(Long idAsiento) {
        this.idAsiento = idAsiento;
    }

    public Integer getFila() {
        return fila;
    }

    public void setFila(Integer fila) {
        this.fila = fila;
    }

    public Integer getColumna() {
        return columna;
    }

    public void setColumna(Integer columna) {
        this.columna = columna;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

}
