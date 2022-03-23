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

import es.seresco.cursojee.videoclub.business.service.ActorService;
import es.seresco.cursojee.videoclub.exception.ElementoNoExistenteException;
import es.seresco.cursojee.videoclub.exception.PeticionInconsistenteException;
import es.seresco.cursojee.videoclub.view.dto.actor.RequestActualizarActorDTO;
import es.seresco.cursojee.videoclub.view.dto.actor.RequestBorrarActorDTO;
import es.seresco.cursojee.videoclub.view.dto.actor.RequestCrearActorDTO;
import es.seresco.cursojee.videoclub.view.dto.actor.ResponseActorDTO;

@RestController
@RequestMapping(path = "/api/privado/actores/")
public class ActorController {

	@Resource
	private ActorService actorService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ResponseActorDTO>> getActores()
	{
		List<ResponseActorDTO> lista = actorService.findAll();
		return ResponseEntity.ok(lista);
	}

	@GetMapping(path = "/{idActor}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseActorDTO> getActor(
			@PathVariable @NotNull @Positive Long idActor)
					throws ElementoNoExistenteException
	{
		ResponseActorDTO actorDTO = actorService.findById(idActor);
		return ResponseEntity.ok(actorDTO);
	}

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseActorDTO> createActor(
			@Validated @RequestBody @NotNull RequestCrearActorDTO requestCrearActorDTO)
	{
		ResponseActorDTO actorDTO = actorService.create(requestCrearActorDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(actorDTO);
	}

	@PutMapping(path = "/{idActor}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> updateActor(
			@PathVariable @NotNull @Positive Long idActor,
			@Validated @RequestBody @NotNull RequestActualizarActorDTO requestActualizarActorDTO)
					throws PeticionInconsistenteException, ElementoNoExistenteException
	{
		if (!idActor.equals(requestActualizarActorDTO.getId())) {
			throw new PeticionInconsistenteException();
		}
		actorService.update(requestActualizarActorDTO);

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(path = "/{idActor}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> delete(
			@PathVariable @NotNull @Positive Long idActor,
			@Validated @RequestBody @NotNull RequestBorrarActorDTO requestBorrarActorDTO)
					throws PeticionInconsistenteException, ElementoNoExistenteException
	{
		if (!idActor.equals(requestBorrarActorDTO.getId())) {
			throw new PeticionInconsistenteException();
		}

		actorService.delete(requestBorrarActorDTO);

		return ResponseEntity.noContent().build();
	}

}