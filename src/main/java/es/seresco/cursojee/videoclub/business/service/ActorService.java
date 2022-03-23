package es.seresco.cursojee.videoclub.business.service;

import java.util.List;

import org.springframework.lang.NonNull;

import es.seresco.cursojee.videoclub.exception.ElementoNoExistenteException;
import es.seresco.cursojee.videoclub.view.dto.actor.RequestActualizarActorDTO;
import es.seresco.cursojee.videoclub.view.dto.actor.RequestBorrarActorDTO;
import es.seresco.cursojee.videoclub.view.dto.actor.RequestCrearActorDTO;
import es.seresco.cursojee.videoclub.view.dto.actor.ResponseActorDTO;

public interface ActorService
{

	static final String BEAN_NAME = "actorService";


	List<ResponseActorDTO> findAll();

	ResponseActorDTO findById(
			final @NonNull Long id) throws ElementoNoExistenteException;

	ResponseActorDTO create(
			final @NonNull RequestCrearActorDTO requestCrearActorDTO);

	ResponseActorDTO update(
			final @NonNull RequestActualizarActorDTO requestActualizarActorDTO) throws ElementoNoExistenteException;

	void delete(
			final @NonNull RequestBorrarActorDTO requestBorrarActorDTO) throws ElementoNoExistenteException;

}
