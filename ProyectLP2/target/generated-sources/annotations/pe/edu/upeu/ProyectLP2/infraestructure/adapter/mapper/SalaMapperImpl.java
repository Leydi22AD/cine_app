package pe.edu.upeu.ProyectLP2.infraestructure.adapter.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pe.edu.upeu.ProyectLP2.domain.model.Sala;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.SalaEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-16T20:26:30-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Amazon.com Inc.)"
)
@Component
public class SalaMapperImpl implements SalaMapper {

    @Override
    public Sala toDomainModel(SalaEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Sala sala = new Sala();

        sala.setIdSala( entity.getIdSala() );
        sala.setNumero( entity.getNumero() );
        sala.setFilas( entity.getFilas() );
        sala.setColumnas( entity.getColumnas() );

        return sala;
    }

    @Override
    public SalaEntity toEntity(Sala domain) {
        if ( domain == null ) {
            return null;
        }

        SalaEntity salaEntity = new SalaEntity();

        salaEntity.setIdSala( domain.getIdSala() );
        salaEntity.setNumero( domain.getNumero() );
        salaEntity.setFilas( domain.getFilas() );
        salaEntity.setColumnas( domain.getColumnas() );

        return salaEntity;
    }

    @Override
    public List<Sala> toDomainModelList(List<SalaEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<Sala> list = new ArrayList<Sala>( entities.size() );
        for ( SalaEntity salaEntity : entities ) {
            list.add( toDomainModel( salaEntity ) );
        }

        return list;
    }
}
