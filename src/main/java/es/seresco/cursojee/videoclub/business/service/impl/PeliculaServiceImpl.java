package es.seresco.cursojee.videoclub.business.service.impl;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Resource;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import es.seresco.cursojee.videoclub.business.model.Actor;
import es.seresco.cursojee.videoclub.business.model.Pelicula;
import es.seresco.cursojee.videoclub.business.service.ActorService;
import es.seresco.cursojee.videoclub.business.service.PeliculaService;
import es.seresco.cursojee.videoclub.exception.ElementoNoExistenteException;
import es.seresco.cursojee.videoclub.mapper.PeliculaMapper;
import es.seresco.cursojee.videoclub.view.dto.Identificable;
import es.seresco.cursojee.videoclub.view.dto.pelicula.RequestActualizarPeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.RequestBorrarPeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.RequestCrearPeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.ResponsePeliculaDTO;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service(PeliculaService.BEAN_NAME)
@Setter
@NoArgsConstructor
@Slf4j
public class PeliculaServiceImpl implements PeliculaService {

	private static final AtomicLong backedIdSeeder = new AtomicLong(0L);
	private static final AtomicReference<List<Pelicula>> backedRef = new AtomicReference<>();


	@Resource
	private PeliculaMapper peliculaMapper;

	@Resource
	private ActorService actorService;


	@Override
	public List<ResponsePeliculaDTO> findAll()
	{
		log.debug("findAll()");
		return peliculaMapper.mapPeliculaToResponsePeliculaDTO(backedReference());
	}

	@Override
	public ResponsePeliculaDTO findById(
			final @NonNull Long id)
					throws ElementoNoExistenteException
	{
		log.debug("findById({})", id);
		Pelicula pelicula = findInternal(id);
		return peliculaMapper.mapPeliculaToResponsePeliculaDTO(pelicula);
	}

	@Override
	public ResponsePeliculaDTO create(
			final @NonNull RequestCrearPeliculaDTO requestCrearPeliculaDTO) throws ElementoNoExistenteException
	{
		log.debug("create({})", requestCrearPeliculaDTO);
		Pelicula pelicula = peliculaMapper.mapRequestCreateDTOToPelicula(requestCrearPeliculaDTO);
		pelicula.setId(nextId());

		for (final Long idActor : requestCrearPeliculaDTO.getActores()) {
			pelicula.addActor(actorService.findModelById(idActor)
					.orElseThrow(ElementoNoExistenteException.creater(
							Pelicula.class, idActor)));
		}

		initBackedReference().add(pelicula);

		return peliculaMapper.mapPeliculaToResponsePeliculaDTO(pelicula);
	}

	@Override
	public ResponsePeliculaDTO update(
			final @NonNull RequestActualizarPeliculaDTO requestActualizarPeliculaDTO)
					throws ElementoNoExistenteException
	{
		log.debug("update({})", requestActualizarPeliculaDTO);
		Long id;
		Objects.requireNonNull(id = requestActualizarPeliculaDTO.getId(), "`peliculaDTO.id` must be non-null");
		Pelicula pelicula = findInternal(id);

		peliculaMapper.updatePeliculaFromDTO(requestActualizarPeliculaDTO, pelicula);
		Collection<Actor> actores = new LinkedHashSet<>();
		for (final Long idActor : requestActualizarPeliculaDTO.getActores()) {
			actores.add(actorService.findModelById(idActor)
					.orElseThrow(ElementoNoExistenteException.creater(
							Pelicula.class, id)));
		}
		synchronized (pelicula) {
			pelicula.removeActores();
			pelicula.addActores(actores);
		}

		return peliculaMapper.mapPeliculaToResponsePeliculaDTO(pelicula);
	}

	@Override
	public void delete(
			final @NonNull RequestBorrarPeliculaDTO requestBorrarPeliculaDTO)
					throws ElementoNoExistenteException
	{
		log.debug("delete({})", requestBorrarPeliculaDTO);
		Long id;
		Objects.requireNonNull(id = requestBorrarPeliculaDTO.getId(), "`peliculaDTO.id` must be non-null");

		final Pelicula pelicula;
		final List<Pelicula> peliculas = backedReference();
		if (peliculas == null || !peliculas.remove(pelicula = findInternal(id))) {
			throw new ElementoNoExistenteException(Pelicula.class, id);
		}
		// perform cascade clean-up to avoid memory leaks
		pelicula.removeActores();
	}


	protected Pelicula findInternal(final Long id)
			throws ElementoNoExistenteException
	{
		final List<Pelicula> peliculas = backedReference();
		if (peliculas != null) {
			return peliculas.stream()
					.filter(Identificable.finder(id))
					.findFirst()
					.orElseThrow(ElementoNoExistenteException.creater(
							Pelicula.class, id));
		}
		throw new ElementoNoExistenteException(Pelicula.class, id);
	}


	protected Long nextId() {
		log.debug("nextId({})", backedIdSeeder.get());
		return backedIdSeeder.incrementAndGet();
	}

	protected List<Pelicula> backedReference() {
//		return backedRef.getAcquire();
//		return backedRef.getOpaque();
//		return backedRef.getPlain();
		return backedRef.get();
	}

	protected List<Pelicula> initBackedReference() {
		log.debug("initBackedReference");
		return backedRef
			// init collection if not yet initialized
			.updateAndGet(ref -> ref == null ? new LinkedList<>(): ref);
	}

}
