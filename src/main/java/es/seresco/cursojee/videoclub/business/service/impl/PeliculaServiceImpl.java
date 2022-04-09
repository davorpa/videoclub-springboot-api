package es.seresco.cursojee.videoclub.business.service.impl;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.seresco.cursojee.videoclub.business.model.Actor;
import es.seresco.cursojee.videoclub.business.model.Pelicula;
import es.seresco.cursojee.videoclub.business.repository.PeliculaRepository;
import es.seresco.cursojee.videoclub.business.service.ActorService;
import es.seresco.cursojee.videoclub.business.service.PeliculaService;
import es.seresco.cursojee.videoclub.exception.MyBusinessException;
import es.seresco.cursojee.videoclub.exception.NoSuchEntityException;
import es.seresco.cursojee.videoclub.mapper.PeliculaMapper;
import es.seresco.cursojee.videoclub.view.dto.pelicula.CustomSearchPeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.RequestActualizarPeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.RequestBorrarPeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.RequestCrearPeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.ResponsePeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.ResponseSearchPeliculaDTO;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service(PeliculaService.BEAN_NAME)
@Transactional(rollbackFor = MyBusinessException.class)
@Setter
@NoArgsConstructor
@Slf4j
public class PeliculaServiceImpl implements PeliculaService
{

	@Resource
	private PeliculaRepository peliculaRepository;

	@Resource
	private PeliculaMapper peliculaMapper;

	@Resource
	private ActorService actorService;


	@Transactional(readOnly = true)
	@Override
	public @NonNull List<ResponsePeliculaDTO> findAll()
	{
		log.debug("findAllPeliculas()");
		return peliculaMapper.mapPeliculaToResponsePeliculaDTO(peliculaRepository.findAll());
	}

	@Transactional(readOnly = true)
	@Override
	public @NonNull List<ResponseSearchPeliculaDTO> search(
			final @Nullable CustomSearchPeliculaDTO query)
	{
		log.debug("searchPeliculas({})", query);
		if (query == null) {
			return peliculaMapper.mapPeliculaToSearchDTO(peliculaRepository.findAll());
		}
		// build predicate
		Predicate<Pelicula> predicate = Objects::nonNull;
		if (query.getTitulo() != null) {
			predicate = predicate.and(pelicula -> containsIgnoreCase(
					pelicula.getTitulo(), query.getTitulo()));
		}
		if (query.getAnio() != null) {
			predicate = predicate.and(pelicula -> Objects.equals(
					pelicula.getAnio(), query.getAnio()));
		}
		if (query.getDuracion() != null) {
			predicate = predicate.and(pelicula -> Objects.equals(
					pelicula.getDuracion(), query.getDuracion()));
		}
		if (query.getActor() != null) {
			final String value = query.getActor();
			final Predicate<Actor> nombrePredicate = actor -> containsIgnoreCase(
					actor.getNombre(), value);
			final Predicate<Actor> apell1Predicate = actor -> containsIgnoreCase(
					actor.getPrimerApellido(), value);
			final Predicate<Actor> apell2Predicate = actor -> containsIgnoreCase(
					actor.getPrimerApellido(), value);
			predicate = predicate.and(pelicula -> !pelicula.findActores(
						nombrePredicate.or(apell1Predicate).or(apell2Predicate)
					).isEmpty());
		}
		// filter an collect
		Collection<Pelicula> peliculas = peliculaRepository.findAll().stream()
				.filter(predicate)
				.collect(Collectors.toCollection(LinkedList::new));
		return peliculaMapper.mapPeliculaToSearchDTO(peliculas);
	}

	@Transactional(readOnly = true)
	@Override
	public @NonNull ResponsePeliculaDTO findById(
			final @NonNull Long id)
					throws NoSuchEntityException
	{
		log.debug("findPeliculaById({})", id);
		Pelicula pelicula = findInternal(id);
		return peliculaMapper.mapPeliculaToResponsePeliculaDTO(pelicula);
	}

	@Override
	public @NonNull ResponsePeliculaDTO create(
			final @NonNull RequestCrearPeliculaDTO requestCrearPeliculaDTO) throws NoSuchEntityException
	{
		log.debug("createPelicula({})", requestCrearPeliculaDTO);
		Pelicula pelicula = peliculaMapper.mapRequestCreateDTOToPelicula(requestCrearPeliculaDTO);

		// map model details
		for (final Long idActor : requestCrearPeliculaDTO.getActores()) {
			Actor actor = actorService.findModelById(idActor)
					.orElseThrow(NoSuchEntityException.creater(
							Pelicula.class, idActor));
			pelicula.addActor(actor);
		}

		pelicula = peliculaRepository.create(pelicula);

		return peliculaMapper.mapPeliculaToResponsePeliculaDTO(pelicula);
	}

	@Override
	public @NonNull ResponsePeliculaDTO update(
			final @NonNull RequestActualizarPeliculaDTO requestActualizarPeliculaDTO)
					throws NoSuchEntityException
	{
		log.debug("updatePelicula({})", requestActualizarPeliculaDTO);
		Long id;
		Objects.requireNonNull(id = requestActualizarPeliculaDTO.getId(), "`peliculaDTO.id` must be non-null");
		// build entity to update
		Pelicula pelicula = Pelicula.builder().id(id).build();
		peliculaMapper.updatePeliculaFromDTO(requestActualizarPeliculaDTO, pelicula);
		for (final Long idActor : requestActualizarPeliculaDTO.getActores()) {
			Actor actor = actorService.findModelById(idActor)
					.orElseThrow(NoSuchEntityException.creater(
							Pelicula.class, idActor));
			pelicula.addActor(actor);
		}
		// update
		try {
			pelicula = peliculaRepository.update(pelicula);
		} catch (NoSuchElementException e) {
			throw new NoSuchEntityException(Pelicula.class, id);
		}
		// transform back to DTO
		return peliculaMapper.mapPeliculaToResponsePeliculaDTO(pelicula);
	}

	@Override
	public void delete(
			final @NonNull RequestBorrarPeliculaDTO requestBorrarPeliculaDTO)
					throws NoSuchEntityException
	{
		log.debug("deletePelicula({})", requestBorrarPeliculaDTO);
		Long id;
		Objects.requireNonNull(id = requestBorrarPeliculaDTO.getId(), "`peliculaDTO.id` must be non-null");

		final Pelicula pelicula = findInternal(id);
		if (pelicula == null || !peliculaRepository.delete(pelicula)) {
			throw new NoSuchEntityException(Pelicula.class, id);
		}
	}


	protected Pelicula findInternal(final Long id)
			throws NoSuchEntityException
	{
		return peliculaRepository.findById(id)
				.orElseThrow(NoSuchEntityException.creater(Pelicula.class, id));
	}

}
