package es.seresco.cursojee.videoclub.business.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.lang.NonNull;

import es.seresco.cursojee.videoclub.business.model.Actor;
import es.seresco.cursojee.videoclub.business.repository.core.CrudRepository;
import es.seresco.cursojee.videoclub.view.dto.Identificable;

public interface ActorRepository extends CrudRepository<Actor, Long>
{

	static final String BEAN_NAME = "actorRepository";

	@Override
	@NonNull Collection<Actor> findAll();

	@Override
	@NonNull default Optional<Actor> findById(final @NonNull Long id) {
		return findAll().stream()
				.filter(Identificable.finder(id))
				.findFirst();
	}

}
