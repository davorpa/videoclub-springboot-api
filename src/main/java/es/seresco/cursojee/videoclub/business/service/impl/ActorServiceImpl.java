package es.seresco.cursojee.videoclub.business.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Resource;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import es.seresco.cursojee.videoclub.business.model.Actor;
import es.seresco.cursojee.videoclub.business.service.ActorService;
import es.seresco.cursojee.videoclub.exception.ElementoNoExistenteException;
import es.seresco.cursojee.videoclub.mapper.ActorMapper;
import es.seresco.cursojee.videoclub.view.dto.Identificable;
import es.seresco.cursojee.videoclub.view.dto.actor.RequestActualizarActorDTO;
import es.seresco.cursojee.videoclub.view.dto.actor.RequestBorrarActorDTO;
import es.seresco.cursojee.videoclub.view.dto.actor.RequestCrearActorDTO;
import es.seresco.cursojee.videoclub.view.dto.actor.ResponseActorDTO;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service(ActorService.BEAN_NAME)
@Setter
@NoArgsConstructor
@Slf4j
public class ActorServiceImpl implements ActorService {

	private static final AtomicLong backedIdSeeder = new AtomicLong(0L);
	private static final AtomicReference<List<Actor>> backedRef = new AtomicReference<>();


	@Resource
	private ActorMapper actorMapper;


	@Override
	public List<ResponseActorDTO> findAll()
	{
		log.debug("findAll");
		return actorMapper.mapActorToResponseActorDTO(backedReference());
	}

	@Override
	public ResponseActorDTO findById(
			final @NonNull Long id)
					throws ElementoNoExistenteException
	{
		log.debug("findById({})", id);
		Actor actor = findInternal(id);
		return actorMapper.mapActorToResponseActorDTO(actor);
	}

	@Override
	public Optional<Actor> findModelById(Long id) {
		try {
			return Optional.ofNullable(findInternal(id));
		} catch (ElementoNoExistenteException e) {
			return Optional.empty();
		}
	}

	@Override
	public ResponseActorDTO create(
			final @NonNull RequestCrearActorDTO requestCrearActorDTO)
	{
		log.debug("create({})", requestCrearActorDTO);
		Actor actor = actorMapper.mapRequestCreateDTOToActor(requestCrearActorDTO);
		actor.setId(nextId());
		initBackedReference().add(actor);
		return actorMapper.mapActorToResponseActorDTO(actor);
	}

	@Override
	public ResponseActorDTO update(
			final @NonNull RequestActualizarActorDTO requestActualizarActorDTO)
					throws ElementoNoExistenteException
	{
		log.debug("update({})", requestActualizarActorDTO);
		Long id;
		Objects.requireNonNull(id = requestActualizarActorDTO.getId(), "`actorDTO.id` must be non-null");
		Actor actor = findInternal(id);
		actorMapper.updateActorFromDTO(requestActualizarActorDTO, actor);
		return actorMapper.mapActorToResponseActorDTO(actor);
	}

	@Override
	public void delete(
			final @NonNull RequestBorrarActorDTO requestBorrarActorDTO)
					throws ElementoNoExistenteException
	{
		log.debug("delete({})", requestBorrarActorDTO);
		Long id;
		Objects.requireNonNull(id = requestBorrarActorDTO.getId(), "`actorDTO.id` must be non-null");
		final List<Actor> actores = backedReference();
		if (actores == null || !actores.remove(findInternal(id))) {
			throw new ElementoNoExistenteException(Actor.class, id);
		}
	}


	protected Actor findInternal(final Long id)
			throws ElementoNoExistenteException
	{
		final List<Actor> actores = backedReference();
		if (actores != null) {
			return actores.stream()
					.filter(Identificable.finder(id))
					.findFirst()
					.orElseThrow(ElementoNoExistenteException.creater(Actor.class, id));
		}
		throw new ElementoNoExistenteException(Actor.class, id);
	}


	protected Long nextId() {
		log.debug("nextId({})", backedIdSeeder.get());
		return backedIdSeeder.incrementAndGet();
	}

	protected List<Actor> backedReference() {
//		return backedRef.getAcquire();
//		return backedRef.getOpaque();
//		return backedRef.getPlain();
		return backedRef.get();
	}

	protected List<Actor> initBackedReference() {
		log.debug("initBackedReference");
		return backedRef
			// init collection if not yet initialized
			.updateAndGet(ref -> ref == null ? new LinkedList<>(): ref);
	}

}
