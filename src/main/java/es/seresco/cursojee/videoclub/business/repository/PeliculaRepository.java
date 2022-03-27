package es.seresco.cursojee.videoclub.business.repository;

import es.seresco.cursojee.videoclub.business.model.Pelicula;
import es.seresco.cursojee.videoclub.business.repository.core.CrudRepository;

public interface PeliculaRepository extends CrudRepository<Pelicula, Long>
{

	static final String BEAN_NAME = "peliculaRepository";

}
