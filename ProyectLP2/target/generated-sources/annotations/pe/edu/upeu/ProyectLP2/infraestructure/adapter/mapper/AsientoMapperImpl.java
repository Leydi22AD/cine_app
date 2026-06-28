package pe.edu.upeu.ProyectLP2.infraestructure.adapter.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.upeu.ProyectLP2.domain.model.Asiento;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.AsientoEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-23T17:40:11-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class AsientoMapperImpl implements AsientoMapper {

    @Autowired
    private SalaMapper salaMapper;

    @Override
    public Asiento toDomainModel(AsientoEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Asiento asiento = new Asiento();

        asiento.setIdAsiento( entity.getIdAsiento() );
        asiento.setFila( entity.getFila() );
        asiento.setColumna( entity.getColumna() );
        asiento.setEstado( entity.getEstado() );
        asiento.setSala( salaMapper.toDomainModel( entity.getSala() ) );

        return asiento;
    }

    @Override
    public AsientoEntity toEntity(Asiento asiento) {
        if ( asiento == null ) {
            return null;
        }

        AsientoEntity asientoEntity = new AsientoEntity();

        asientoEntity.setIdAsiento( asiento.getIdAsiento() );
        asientoEntity.setFila( asiento.getFila() );
        asientoEntity.setEstado( asiento.getEstado() );
        asientoEntity.setColumna( asiento.getColumna() );
        asientoEntity.setSala( salaMapper.toEntity( asiento.getSala() ) );

        return asientoEntity;
    }

    @Override
    public List<Asiento> toDomainModelList(List<AsientoEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<Asiento> list = new ArrayList<Asiento>( entities.size() );
        for ( AsientoEntity asientoEntity : entities ) {
            list.add( toDomainModel( asientoEntity ) );
        }

        return list;
    }
}
