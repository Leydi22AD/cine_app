package pe.edu.upeu.ProyectLP2.infraestructure.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "pelicula")
public class PeliculaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPelicula;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(nullable = false)
    private String genero;

    @Column(nullable = false)
    private Integer duracion;

    @Column(nullable = false)
    private String formato;

    @Column(nullable = false)
    private String idioma;

    @Column(length = 250)
    private String poster; // URL o ruta de la imagen

    @Column(length = 100)
    private String director;

    @Column(nullable = false, length = 200)
    private String descripcion;

    @Column(length = 50)
    private String trailer;

    @OneToMany(mappedBy = "pelicula", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<FuncionEntity> funciones;

    public PeliculaEntity() {
    }

    public PeliculaEntity(Long idPelicula, String titulo, String genero, Integer duracion, String formato, String idioma, String descripcion, String poster, String director, String trailer, List<FuncionEntity> funciones) {
        this.idPelicula = idPelicula;
        this.titulo = titulo;
        this.genero = genero;
        this.duracion = duracion;
        this.formato = formato;
        this.idioma = idioma;
        this.descripcion = descripcion;
        this.poster = poster;
        this.director = director;
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

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
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

    public List<FuncionEntity> getFunciones() {
        return funciones;
    }

    public void setFunciones(List<FuncionEntity> funciones) {
        this.funciones = funciones;
    }
}