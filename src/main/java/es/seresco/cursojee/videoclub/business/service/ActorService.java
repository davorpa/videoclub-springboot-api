package es.seresco.cursojee.videoclub.business.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;

import es.seresco.cursojee.videoclub.business.model.Actor;
import es.seresco.cursojee.videoclub.exception.NoSuchEntityException;
import es.seresco.cursojee.videoclub.view.dto.actor.RequestActualizarActorDTO;
import es.seresco.cursojee.videoclub.view.dto.actor.RequestBorrarActorDTO;
import es.seresco.cursojee.videoclub.view.dto.actor.RequestCrearActorDTO;
import es.seresco.cursojee.videoclub.view.dto.actor.ResponseActorDTO;

@Validated
public interface ActorService
{

	static final String BEAN_NAME = "actorService";


	@NonNull List<ResponseActorDTO> findAll();

	@NonNull ResponseActorDTO findById(
			final @NotNull @NonNull Long id)
					throws NoSuchEntityException;

	@NonNull Optional<Actor> findModelById(
			final @NotNull @NonNull Long id);

	@NonNull ResponseActorDTO create(
			final @Valid @NotNull @NonNull RequestCrearActorDTO requestCrearActorDTO);

	@NonNull ResponseActorDTO update(
			final @Valid @NotNull @NonNull RequestActualizarActorDTO requestActualizarActorDTO)
					throws NoSuchEntityException;

	void delete(
			final @Valid @NotNull @NonNull RequestBorrarActorDTO requestBorrarActorDTO)
					throws NoSuchEntityException;

}
