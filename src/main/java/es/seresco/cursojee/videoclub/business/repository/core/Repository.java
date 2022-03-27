package es.seresco.cursojee.videoclub.business.repository.core;

import java.io.Serializable;

import es.seresco.cursojee.videoclub.business.model.Entity;
import es.seresco.cursojee.videoclub.view.dto.Identificable;

/**
 * Contrato de marca para acceso a datos del modelo de entidades.
 *
 * @param <T> - tipo de dato que define la entidad del modelo
 * @param <ID> - tipo de dato con el que se identifica la entidad
 * @see Entity
 * @see Identificable
 */
public interface Repository<T extends Entity<ID> & Serializable, ID extends Serializable> // NOSONAR
{

}
