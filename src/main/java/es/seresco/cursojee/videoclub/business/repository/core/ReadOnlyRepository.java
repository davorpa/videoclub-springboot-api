package es.seresco.cursojee.videoclub.business.repository.core;

import java.io.Serializable;

import es.seresco.cursojee.videoclub.business.model.Entity;

/**
 * Repository que define genéricamente llas operaciones de lectura más importantes
 * para un tipo de dato concreto.
 *
 * <li>Retrieve all
 * <li>Retrieve one
 * <li>Check existence
 * <li>Count
 *
 * @param <T> - tipo de dato que representa la entidad de dominio
 * @param <ID> - tipo de dato del campo que identifica unequívocamente dicha entidad
 * @see RepositoryReadableOperations
 */
public interface ReadOnlyRepository<T extends Entity<ID> & Serializable, ID extends Serializable>
		extends Repository<T, ID>,
				RepositoryReadableOperations<T, ID>
{

}
