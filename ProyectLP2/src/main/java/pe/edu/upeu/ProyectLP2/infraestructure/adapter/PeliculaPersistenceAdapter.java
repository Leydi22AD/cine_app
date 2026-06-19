package pe.edu.upeu.ProyectLP2.infraestructure.adapter;


import org.springframework.stereotype.Component;
import pe.edu.upeu.ProyectLP2.domain.model.Pelicula;
import pe.edu.upeu.ProyectLP2.domain.port.on.PeliculaRepositoryPort;
import pe.edu.upeu.ProyectLP2.infraestructure.adapter.mapper.PeliculaMapper;
import pe.edu.upeu.ProyectLP2.infraestructure.entity.PeliculaEntity;

import java.util.List;
import java.util.Optional;

@Component
public class PeliculaPersistenceAdapter implements PeliculaRepositoryPort {

    private final PeliculaRepository peliculaRepository;
    private final PeliculaMapper peliculaMapper;


    public PeliculaPersistenceAdapter(PeliculaRepository peliculaRepository, PeliculaMapper peliculaMapper) {
        this.peliculaRepository = peliculaRepository;
        this.peliculaMapper = peliculaMapper;
    }

    @Override
    public Pelicula save(Pelicula pelicula) {
        PeliculaEntity entity = peliculaMapper.toEntity(pelicula);
        PeliculaEntity saved = peliculaRepository.save(entity);
        return peliculaMapper.toDomainModel(saved);
    }

    @Override
    public Optional<Pelicula> findById(Long id) {
        return peliculaRepository.findById(id)
                .map(peliculaMapper::toDomainModel);    }

    @Override
    public List<Pelicula> findAll() {
        return peliculaMapper.toDomainModelList(peliculaRepository.findAll());
    }


    @Override
    public List<Pelicula> findByTitulo(String titulo) {
        return peliculaMapper.toDomainModelList(peliculaRepository.findByTitulo(titulo));
    }

    @Override
    public void deleteById(Long id) {
        peliculaRepository.deleteById(id);
    }

    @Override
    public Pelicula update(Long id, Pelicula pelicula) {
        PeliculaEntity existing = peliculaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Película no encontrada con id " + id));

        // copiar campos (sin tocar el id):
        existing.setTitulo(pelicula.getTitulo());
        existing.setGenero(pelicula.getGenero());
        existing.setDuracion(pelicula.getDuracion());
        existing.setFormato(pelicula.getFormato());
        existing.setIdioma(pelicula.getIdioma());
        existing.setPoster(pelicula.getPoster());
        existing.setDescripcion(pelicula.getDescripcion());
        existing.setTrailer(pelicula.getTrailer());

        PeliculaEntity saved = peliculaRepository.save(existing);
        return peliculaMapper.toDomainModel(saved);
}

}
