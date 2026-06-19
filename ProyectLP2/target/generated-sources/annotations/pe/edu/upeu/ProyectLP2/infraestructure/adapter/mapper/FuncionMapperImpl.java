package pe.edu.upeu.ProyectLP2.infraestructure.adapter.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.upeu.ProyectLP2.domain.model.Funcion;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.FuncionEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-16T20:26:29-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Amazon.com Inc.)"
)
@Component
public class FuncionMapperImpl implements FuncionMapper {

    @Autowired
    private SalaMapper salaMapper;
    @Autowired
    private PeliculaMapper peliculaMapper;

    @Override
    public Funcion toDomainModel(FuncionEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Funcion funcion = new Funcion();

        funcion.setIdFuncion( entity.getIdFuncion() );
        funcion.setPelicula( peliculaMapper.toDomainModel( entity.getPelicula() ) );
        funcion.setSala( salaMapper.toDomainModel( entity.getSala() ) );
        funcion.setFecha( entity.getFecha() );
        funcion.setPrecio( entity.getPrecio() );

        return funcion;
    }

    @Override
    public FuncionEntity toEntity(Funcion domain) {
        if ( domain == null ) {
            return null;
        }

        FuncionEntity funcionEntity = new FuncionEntity();

        funcionEntity.setIdFuncion( domain.getIdFuncion() );
        funcionEntity.setPelicula( peliculaMapper.toEntity( domain.getPelicula() ) );
        funcionEntity.setSala( salaMapper.toEntity( domain.getSala() ) );
        funcionEntity.setFecha( domain.getFecha() );
        funcionEntity.setPrecio( domain.getPrecio() );

        return funcionEntity;
    }

    @Override
    public List<Funcion> toDomainModelList(List<FuncionEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<Funcion> list = new ArrayList<Funcion>( entities.size() );
        for ( FuncionEntity funcionEntity : entities ) {
            list.add( toDomainModel( funcionEntity ) );
        }

        return list;
    }
}
