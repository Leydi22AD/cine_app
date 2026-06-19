package pe.edu.upeu.ProyectLP2.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties({"funciones"})
public class Pelicula {

    private Long idPelicula;
    private String titulo;
    private String genero;
    private Integer duracion;
    private String formato;
    private String idioma;
    private String poster;
    private String director;
    private String descripcion;
    private String trailer;
    private List<Funcion> funciones;

    public Pelicula() {
    }

    public Pelicula(Long idPelicula, String titulo, Integer duracion, String genero, String idioma, String formato, String poster, String director, String descripcion, String trailer, List<Funcion> funciones) {
        this.idPelicula = idPelicula;
        this.titulo = titulo;
        this.duracion = duracion;
        this.genero = genero;
        this.idioma = idioma;
        this.formato = formato;
        this.poster = poster;
        this.director = director;
        this.descripcion = descripcion;
        this.trailer = trailer;
        this.funciones = funciones;
    }

    public Long getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(Long idPelicula) {
        this.idPelicula = idPelicula;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    // Alias para compatibilidad con frontend
    public String getUrlPoster() {
        return poster;
    }

    public String getUrlTrailer() {
        return trailer;
    }

    public List<Funcion> getFunciones() {
        return funciones;
    }

    public void setFunciones(List<Funcion> funciones) {
        this.funciones = funciones;
    }
}
