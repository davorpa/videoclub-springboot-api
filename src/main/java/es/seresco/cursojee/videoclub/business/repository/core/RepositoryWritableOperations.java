package es.seresco.cursojee.videoclub.business.repository.core;

import org.springframework.lang.NonNull;

/**
 * Contrato que define genéricamente las operaciones de escritura más importantes
 * para un tipo de dato concreto.
 *
 * <li>Create
 * <li>Update
 * <li>Delete
 *
 * @param <T> - tipo de dato que representa la entidad de dominio
 * @param <ID> - tipo de dato del campo que identifica unequívocamente dicha entidad
 */
public interface RepositoryWritableOperations<T, ID> // NOSONAR
{

	@NonNull <S extends T> S create(final @NonNull S entity);

	@NonNull <S extends T> S update(final @NonNull S entity);

	@NonNull boolean delete(final @NonNull T entity);

	@NonNull boolean deleteById(final @NonNull ID id);

}
