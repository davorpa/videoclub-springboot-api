package es.seresco.cursojee.videoclub.business.service.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.seresco.cursojee.videoclub.business.model.Actor;
import es.seresco.cursojee.videoclub.business.model.Pelicula;
import es.seresco.cursojee.videoclub.business.repository.PeliculaRepository;
import es.seresco.cursojee.videoclub.business.service.ActorService;
import es.seresco.cursojee.videoclub.business.service.PeliculaService;
import es.seresco.cursojee.videoclub.exception.ElementoNoExistenteException;
import es.seresco.cursojee.videoclub.mapper.PeliculaMapper;
import es.seresco.cursojee.videoclub.view.dto.pelicula.RequestActualizarPeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.RequestBorrarPeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.RequestCrearPeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.ResponsePeliculaDTO;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service(PeliculaService.BEAN_NAME)
@Transactional(rollbackFor = ElementoNoExistenteException.class)
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
	public List<ResponsePeliculaDTO> findAll()
	{
		log.debug("findAllPeliculas()");
		return peliculaMapper.mapPeliculaToResponsePeliculaDTO(peliculaRepository.findAll());
	}

	@Transactional(readOnly = true)
	@Override
	public ResponsePeliculaDTO findById(
			final @NonNull Long id)
					throws ElementoNoExistenteException
	{
		log.debug("findPeliculaById({})", id);
		Pelicula pelicula = findInternal(id);
		return peliculaMapper.mapPeliculaToResponsePeliculaDTO(pelicula);
	}

	@Override
	public ResponsePeliculaDTO create(
			final @NonNull RequestCrearPeliculaDTO requestCrearPeliculaDTO) throws ElementoNoExistenteException
	{
		log.debug("createPelicula({})", requestCrearPeliculaDTO);
		Pelicula pelicula = peliculaMapper.mapRequestCreateDTOToPelicula(requestCrearPeliculaDTO);

		// map model details
		for (final Long idActor : requestCrearPeliculaDTO.getActores()) {
			Actor actor = actorService.findModelById(idActor)
					.orElseThrow(ElementoNoExistenteException.creater(
							Pelicula.class, idActor));
			pelicula.addActor(actor);
		}

		pelicula = peliculaRepository.create(pelicula);

		return peliculaMapper.mapPeliculaToResponsePeliculaDTO(pelicula);
	}

	@Override
	public ResponsePeliculaDTO update(
			final @NonNull RequestActualizarPeliculaDTO requestActualizarPeliculaDTO)
					throws ElementoNoExistenteException
	{
		log.debug("updatePelicula({})", requestActualizarPeliculaDTO);
		Long id;
		Objects.requireNonNull(id = requestActualizarPeliculaDTO.getId(), "`peliculaDTO.id` must be non-null");
		// build entity to update
		Pelicula pelicula = Pelicula.builder().id(id).build();
		peliculaMapper.updatePeliculaFromDTO(requestActualizarPeliculaDTO, pelicula);
		for (final Long idActor : requestActualizarPeliculaDTO.getActores()) {
			Actor actor = actorService.findModelById(idActor)
					.orElseThrow(ElementoNoExistenteException.creater(
							Pelicula.class, idActor));
			pelicula.addActor(actor);
		}
		// update
		try {
			pelicula = peliculaRepository.update(pelicula);
		} catch (NoSuchElementException e) {
			throw new ElementoNoExistenteException(Pelicula.class, id);
		}
		// transform back to DTO
		return peliculaMapper.mapPeliculaToResponsePeliculaDTO(pelicula);
	}

	@Override
	public void delete(
			final @NonNull RequestBorrarPeliculaDTO requestBorrarPeliculaDTO)
					throws ElementoNoExistenteException
	{
		log.debug("deletePelicula({})", requestBorrarPeliculaDTO);
		Long id;
		Objects.requireNonNull(id = requestBorrarPeliculaDTO.getId(), "`peliculaDTO.id` must be non-null");

		final Pelicula pelicula = findInternal(id);
		if (pelicula == null || !peliculaRepository.delete(pelicula)) {
			throw new ElementoNoExistenteException(Pelicula.class, id);
		}
	}


	protected Pelicula findInternal(final Long id)
			throws ElementoNoExistenteException
	{
		return peliculaRepository.findById(id)
				.orElseThrow(ElementoNoExistenteException.creater(Pelicula.class, id));
	}

}
