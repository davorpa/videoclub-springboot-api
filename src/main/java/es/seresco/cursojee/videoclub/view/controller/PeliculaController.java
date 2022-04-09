package es.seresco.cursojee.videoclub.view.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.seresco.cursojee.videoclub.business.service.PeliculaService;
import es.seresco.cursojee.videoclub.exception.NoSuchEntityException;
import es.seresco.cursojee.videoclub.exception.BadRequestException;
import es.seresco.cursojee.videoclub.view.dto.pelicula.CustomSearchPeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.RequestActualizarPeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.RequestBorrarPeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.RequestCrearPeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.ResponsePeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.ResponseSearchPeliculaDTO;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/api/privado/peliculas/")
@Slf4j
public class PeliculaController {

	@Resource
	private PeliculaService peliculaService;

	@ResponseStatus(HttpStatus.OK)
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<ResponsePeliculaDTO> getPeliculas()
	{
		log.info("Listing peliculas...");
		return peliculaService.findAll();
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE, params = "q")
	public @ResponseBody List<ResponseSearchPeliculaDTO> search(
			final @RequestParam("q") String qs)
	{
		log.info("Searching using query string... `{}`", qs);
		// TODO support a search by a "q" param to have a fluent form:
		//
		//         title:"Film" AND actor:*"ie" OR year:>1900 ...
		//
		//      where ":" represents the separator and equal operator
		//            ">" great than
		//            "*" ilike operator
		CustomSearchPeliculaDTO query = CustomSearchPeliculaDTO.builder().build();
		return peliculaService.search(query);
	}


	@ResponseStatus(HttpStatus.OK)
	@GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<ResponseSearchPeliculaDTO> search(
				final @RequestParam(value = "title",    required = false) String titulo,
				final @RequestParam(value = "duration", required = false) Integer duracion,
				final @RequestParam(value = "year",     required = false) Integer anio,
				final @RequestParam(value = "actor",    required = false) String actor)
	{
		log.info("Searching using fixed values...");
		CustomSearchPeliculaDTO query = CustomSearchPeliculaDTO.builder()
				.titulo(titulo)
				.duracion(duracion)
				.anio(anio)
				.actor(actor)
				.build();
		return peliculaService.search(query);
	}

	@GetMapping(path = "/{idPelicula}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponsePeliculaDTO> getPelicula(
			@PathVariable @NotNull @Positive Long idPelicula)
					throws NoSuchEntityException
	{
		ResponsePeliculaDTO peliculaDTO = peliculaService.findById(idPelicula);
		return ResponseEntity.ok(peliculaDTO);
	}

	@Secured({ "ROLE_ADMIN" })
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponsePeliculaDTO> createPelicula(
			@Validated @RequestBody @NotNull RequestCrearPeliculaDTO requestCrearPeliculaDTO)
					throws NoSuchEntityException
	{
		ResponsePeliculaDTO peliculaDTO = peliculaService.create(requestCrearPeliculaDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(peliculaDTO);
	}

	@RolesAllowed({ "ROLE_ADMIN" })
	@PutMapping(path = "/{idPelicula}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> updatePelicula(
			@PathVariable @NotNull @Positive Long idPelicula,
			@Validated @RequestBody @NotNull RequestActualizarPeliculaDTO requestActualizarPeliculaDTO)
					throws BadRequestException, NoSuchEntityException
	{
		if (!idPelicula.equals(requestActualizarPeliculaDTO.getId())) {
			throw new BadRequestException();
		}
		peliculaService.update(requestActualizarPeliculaDTO);

		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping(path = "/{idPelicula}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> delete(
			@PathVariable @NotNull @Positive Long idPelicula,
			@Validated @RequestBody @NotNull RequestBorrarPeliculaDTO requestBorrarPeliculaDTO)
					throws BadRequestException, NoSuchEntityException
	{
		if (!idPelicula.equals(requestBorrarPeliculaDTO.getId())) {
			throw new BadRequestException();
		}

		peliculaService.delete(requestBorrarPeliculaDTO);

		return ResponseEntity.noContent().build();
	}

}
