package es.seresco.cursojee.videoclub.business.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.lang.NonNull;

import es.seresco.cursojee.videoclub.business.model.Pelicula;
import es.seresco.cursojee.videoclub.business.repository.core.CrudRepository;
import es.seresco.cursojee.videoclub.view.dto.Identificable;

public interface PeliculaRepository extends CrudRepository<Pelicula, Long>
{

	static final String BEAN_NAME = "peliculaRepository";

	@Override
	@NonNull Collection<Pelicula> findAll();

	@Override
	@NonNull default Optional<Pelicula> findById(final @NonNull Long id) {
		return findAll().stream()
				.filter(Identificable.finder(id))
				.findFirst();
	}

}
