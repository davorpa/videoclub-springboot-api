package es.seresco.cursojee.videoclub.business.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.lang.NonNull;

import es.seresco.cursojee.videoclub.business.model.Genero;
import es.seresco.cursojee.videoclub.business.repository.core.ReadOnlyRepository;
import es.seresco.cursojee.videoclub.view.dto.Identificable;
import es.seresco.cursojee.videoclub.view.dto.genero.CodigoGeneroEnum;

public interface GeneroRepository extends ReadOnlyRepository<Genero, CodigoGeneroEnum>
{

	static final String BEAN_NAME = "generoRepository";

	@Override
	@NonNull Collection<Genero> findAll();

	@Override
	@NonNull default Optional<Genero> findById(final @NonNull CodigoGeneroEnum id) {
		return findAll().stream()
				.filter(Identificable.finder(id))
				.findFirst();
	}

}
