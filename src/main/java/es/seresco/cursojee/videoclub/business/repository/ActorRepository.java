package es.seresco.cursojee.videoclub.business.repository;

import es.seresco.cursojee.videoclub.business.model.Actor;
import es.seresco.cursojee.videoclub.business.repository.core.CrudRepository;

public interface ActorRepository extends CrudRepository<Actor, Long>
{

	static final String BEAN_NAME = "actorRepository";

}
