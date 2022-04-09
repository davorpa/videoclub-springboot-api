package es.seresco.cursojee.videoclub.business.service.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.seresco.cursojee.videoclub.business.model.Actor;
import es.seresco.cursojee.videoclub.business.repository.ActorRepository;
import es.seresco.cursojee.videoclub.business.service.ActorService;
import es.seresco.cursojee.videoclub.exception.MyBusinessException;
import es.seresco.cursojee.videoclub.exception.NoSuchEntityException;
import es.seresco.cursojee.videoclub.mapper.ActorMapper;
import es.seresco.cursojee.videoclub.view.dto.actor.RequestActualizarActorDTO;
import es.seresco.cursojee.videoclub.view.dto.actor.RequestBorrarActorDTO;
import es.seresco.cursojee.videoclub.view.dto.actor.RequestCrearActorDTO;
import es.seresco.cursojee.videoclub.view.dto.actor.ResponseActorDTO;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service(ActorService.BEAN_NAME)
@Transactional(rollbackFor = MyBusinessException.class)
@Setter
@NoArgsConstructor
@Slf4j
public class ActorServiceImpl implements ActorService
{

	@Resource
	private ActorRepository actorRepository;

	@Resource
	private ActorMapper actorMapper;


	@Transactional(readOnly = true)
	@Override
	public @NonNull List<ResponseActorDTO> findAll()
	{
		log.debug("findAllActores");
		return actorMapper.mapActorToResponseActorDTO(actorRepository.findAll());
	}

	@Transactional(readOnly = true)
	@Override
	public @NonNull ResponseActorDTO findById(
			final @NonNull Long id)
					throws NoSuchEntityException
	{
		log.debug("findActorById({})", id);
		Actor actor = findInternal(id);
		return actorMapper.mapActorToResponseActorDTO(actor);
	}

	@Transactional(readOnly = true)
	@Override
	public @NonNull Optional<Actor> findModelById(Long id) {
		log.debug("findActorById({})", id);
		return actorRepository.findById(id);
	}

	@Override
	public @NonNull ResponseActorDTO create(
			final @NonNull RequestCrearActorDTO requestCrearActorDTO)
	{
		log.debug("createActor({})", requestCrearActorDTO);
		Actor actor = actorMapper.mapRequestCreateDTOToActor(requestCrearActorDTO);
		actor = actorRepository.create(actor);
		return actorMapper.mapActorToResponseActorDTO(actor);
	}

	@Override
	public @NonNull ResponseActorDTO update(
			final @NonNull RequestActualizarActorDTO requestActualizarActorDTO)
					throws NoSuchEntityException
	{
		log.debug("updateActor({})", requestActualizarActorDTO);
		Long id;
		Objects.requireNonNull(id = requestActualizarActorDTO.getId(), "`actorDTO.id` must be non-null");
		// build entity to update
		Actor actor = Actor.builder().id(id).build();
		actorMapper.updateActorFromDTO(requestActualizarActorDTO, actor);
		// update
		try {
			actor = actorRepository.update(actor);
		} catch (NoSuchElementException e) {
			throw new NoSuchEntityException(Actor.class, id);
		}
		// transform back to DTO
		return actorMapper.mapActorToResponseActorDTO(actor);
	}

	@Override
	public void delete(
			final @NonNull RequestBorrarActorDTO requestBorrarActorDTO)
					throws NoSuchEntityException
	{
		log.debug("deleteActor({})", requestBorrarActorDTO);
		Long id;
		Objects.requireNonNull(id = requestBorrarActorDTO.getId(), "`actorDTO.id` must be non-null");

		Actor actor = findInternal(id);
		if (actor == null || !actorRepository.delete(actor)) {
			throw new NoSuchEntityException(Actor.class, id);
		}
	}

	protected Actor findInternal(Long id)
			throws NoSuchEntityException
	{
		return findModelById(id)
				.orElseThrow(NoSuchEntityException.creater(Actor.class, id));
	}

}
