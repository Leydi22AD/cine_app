package pe.edu.upeu.ProyectLP2.infraestructure.adapter.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pe.edu.upeu.ProyectLP2.domain.model.Pelicula;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.PeliculaEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-23T17:40:11-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class PeliculaMapperImpl implements PeliculaMapper {

    @Override
    public Pelicula toDomainModel(PeliculaEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Pelicula pelicula = new Pelicula();

        pelicula.setIdPelicula( entity.getIdPelicula() );
        pelicula.setTitulo( entity.getTitulo() );
        pelicula.setGenero( entity.getGenero() );
        pelicula.setFormato( entity.getFormato() );
        pelicula.setDuracion( entity.getDuracion() );
        pelicula.setIdioma( entity.getIdioma() );
        pelicula.setPoster( entity.getPoster() );
        pelicula.setDirector( entity.getDirector() );
        pelicula.setDescripcion( entity.getDescripcion() );
        pelicula.setTrailer( entity.getTrailer() );

        return pelicula;
    }

    @Override
    public PeliculaEntity toEntity(Pelicula domain) {
        if ( domain == null ) {
            return null;
        }

        PeliculaEntity peliculaEntity = new PeliculaEntity();

        peliculaEntity.setIdPelicula( domain.getIdPelicula() );
        peliculaEntity.setTitulo( domain.getTitulo() );
        peliculaEntity.setGenero( domain.getGenero() );
        peliculaEntity.setDuracion( domain.getDuracion() );
        peliculaEntity.setFormato( domain.getFormato() );
        peliculaEntity.setIdioma( domain.getIdioma() );
        peliculaEntity.setPoster( domain.getPoster() );
        peliculaEntity.setDirector( domain.getDirector() );
        peliculaEntity.setDescripcion( domain.getDescripcion() );
        peliculaEntity.setTrailer( domain.getTrailer() );

        return peliculaEntity;
    }

    @Override
    public List<Pelicula> toDomainModelList(List<PeliculaEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<Pelicula> list = new ArrayList<Pelicula>( entities.size() );
        for ( PeliculaEntity peliculaEntity : entities ) {
            list.add( toDomainModel( peliculaEntity ) );
        }

        return list;
    }
}
