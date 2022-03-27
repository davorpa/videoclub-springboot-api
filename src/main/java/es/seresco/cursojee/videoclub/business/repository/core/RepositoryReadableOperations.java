package es.seresco.cursojee.videoclub.business.repository.core;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.lang.NonNull;

/**
 * Contrato que define genéricamente las operaciones de lectura más importantes
 * para un tipo de dato concreto.
 *
 * <li>Retrieve all
 * <li>Retrieve one
 * <li>Check existence
 * <li>Count
 *
 * @param <T> - tipo de dato que representa la entidad de dominio
 * @param <ID> - tipo de dato del campo que identifica unequívocamente dicha entidad
 */
public interface RepositoryReadableOperations<T, ID> // NOSONAR
{

	@NonNull Iterable<T> findAll();

	@NonNull Optional<T> findById(final @NonNull ID id);

	@NonNull default boolean existsById(@NonNull final ID id) {
		return findById(id).isPresent();
	}

	@NonNull default long count() {
		final AtomicLong counter = new AtomicLong(0);
		this.findAll().forEach(t -> counter.incrementAndGet());
		return counter.get();
	}

}
