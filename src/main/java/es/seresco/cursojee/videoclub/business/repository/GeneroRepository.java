package es.seresco.cursojee.videoclub.business.repository;

import java.util.Collection;

import org.springframework.lang.NonNull;

import es.seresco.cursojee.videoclub.business.model.Genero;
import es.seresco.cursojee.videoclub.business.repository.core.ReadOnlyRepository;
import es.seresco.cursojee.videoclub.view.dto.genero.CodigoGeneroEnum;

public interface GeneroRepository extends ReadOnlyRepository<Genero, CodigoGeneroEnum>
{

	static final String BEAN_NAME = "generoRepository";

	@Override
	@NonNull Collection<Genero> findAll();

}
