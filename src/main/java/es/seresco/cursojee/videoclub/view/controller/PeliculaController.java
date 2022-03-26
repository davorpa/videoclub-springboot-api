package es.seresco.cursojee.videoclub.view.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.seresco.cursojee.videoclub.business.service.PeliculaService;
import es.seresco.cursojee.videoclub.exception.ElementoNoExistenteException;
import es.seresco.cursojee.videoclub.exception.PeticionInconsistenteException;
import es.seresco.cursojee.videoclub.view.dto.pelicula.RequestActualizarPeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.RequestBorrarPeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.RequestCrearPeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.ResponsePeliculaDTO;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/api/privado/peliculas/")
@Slf4j
public class PeliculaController {

	@Resource
	private PeliculaService peliculaService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ResponsePeliculaDTO>> getPeliculas()
	{
		List<ResponsePeliculaDTO> lista = peliculaService.findAll();
		return ResponseEntity.ok(lista);
	}

	@GetMapping(path = "/{idPelicula}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponsePeliculaDTO> getPelicula(
			@PathVariable @NotNull @Positive Long idPelicula)
					throws ElementoNoExistenteException
	{
		ResponsePeliculaDTO peliculaDTO = peliculaService.findById(idPelicula);
		return ResponseEntity.ok(peliculaDTO);
	}

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponsePeliculaDTO> createPelicula(
			@Validated @RequestBody @NotNull RequestCrearPeliculaDTO requestCrearPeliculaDTO)
					throws ElementoNoExistenteException
	{
		ResponsePeliculaDTO peliculaDTO = peliculaService.create(requestCrearPeliculaDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(peliculaDTO);
	}

	@PutMapping(path = "/{idPelicula}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> updatePelicula(
			@PathVariable @NotNull @Positive Long idPelicula,
			@Validated @RequestBody @NotNull RequestActualizarPeliculaDTO requestActualizarPeliculaDTO)
					throws PeticionInconsistenteException, ElementoNoExistenteException
	{
		if (!idPelicula.equals(requestActualizarPeliculaDTO.getId())) {
			throw new PeticionInconsistenteException();
		}
		peliculaService.update(requestActualizarPeliculaDTO);

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(path = "/{idPelicula}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> delete(
			@PathVariable @NotNull @Positive Long idPelicula,
			@Validated @RequestBody @NotNull RequestBorrarPeliculaDTO requestBorrarPeliculaDTO)
					throws PeticionInconsistenteException, ElementoNoExistenteException
	{
		if (!idPelicula.equals(requestBorrarPeliculaDTO.getId())) {
			throw new PeticionInconsistenteException();
		}

		peliculaService.delete(requestBorrarPeliculaDTO);

		return ResponseEntity.noContent().build();
	}

}
