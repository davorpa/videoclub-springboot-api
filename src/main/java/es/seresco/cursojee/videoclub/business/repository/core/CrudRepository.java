package es.seresco.cursojee.videoclub.business.repository.core;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.lang.NonNull;

import es.seresco.cursojee.videoclub.business.model.Entity;

/**
 * Repository que define genéricamente las operaciones CRUD más importantes
 * para un tipo de dato concreto.
 *
 * <li>Create
 * <li>Retrieve (all, one, existence, count... and other read-only operations)
 * <li>Update
 * <li>Delete
 *
 * @param <T> - tipo de dato que representa la entidad de dominio
 * @param <ID> - tipo de dato del campo que identifica unequívocamente dicha entidad
 */
public interface CrudRepository<T extends Entity<ID> & Serializable, ID extends Serializable>
		extends Repository<T, ID>,
				RepositoryReadableOperations<T, ID>,
				RepositoryWritableOperations<T, ID>
{

	@Override
	default boolean deleteById(final @NonNull ID id) {
		final Optional<T> entity = findById(id);
		return entity.isPresent() && delete(entity.get());
	}

}
