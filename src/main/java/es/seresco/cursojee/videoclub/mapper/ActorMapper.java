package es.seresco.cursojee.videoclub.mapper;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import es.seresco.cursojee.videoclub.business.model.Actor;
import es.seresco.cursojee.videoclub.view.dto.Identificable;
import es.seresco.cursojee.videoclub.view.dto.actor.ActorDTO;
import es.seresco.cursojee.videoclub.view.dto.actor.RequestActualizarActorDTO;
import es.seresco.cursojee.videoclub.view.dto.actor.RequestCrearActorDTO;
import es.seresco.cursojee.videoclub.view.dto.actor.ResponseActorDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.PeliculaActorDTO;

@Mapper(componentModel = "spring")
public interface ActorMapper
{

	static final String ACTOR_DTO_TO_ACTOR = "actorDTOToActor";
	static final String EXTRACT_ACTOR_IDS = "extractActorIds";
	static final String ACTOR_TO_PELICULA_ACTOR_DTO  = "Actor#toPeliculaActorDTO";

	//
	// FROM DTO TO ENTITY
	//

	@Named(ACTOR_DTO_TO_ACTOR)
	@Mapping(source = "nombre", target = "nombre")
	@Mapping(source = "primerApellido", target = "primerApellido")
	@Mapping(source = "segundoApellido", target = "segundoApellido")
	@Mapping(source = "fechaNacimiento", target = "fechaNacimiento")
	Actor actorDTOToActor(
			final ActorDTO actorDTO);

	@InheritConfiguration(name = ACTOR_DTO_TO_ACTOR)
	void updateActorFromDTO(
			final RequestActualizarActorDTO requestActualizarActorDTO,
			final @MappingTarget Actor actor);

	@InheritConfiguration(name = ACTOR_DTO_TO_ACTOR)
	Actor mapRequestCreateDTOToActor(
			final RequestCrearActorDTO requestCrearActorDTO);

	@InheritConfiguration(name = ACTOR_DTO_TO_ACTOR)
	@Mapping(source = "id", target = "id")
	Actor mapRequestUpdateDTOToActor(
			final RequestActualizarActorDTO requestActualizarActorDTO);

	//
	// FROM ENTITY TO DTO
	//

	@Mapping(source = "id", target = "id")
	@Mapping(source = "nombre", target = "nombre")
	@Mapping(source = "primerApellido", target = "primerApellido")
	@Mapping(source = "segundoApellido", target = "segundoApellido")
	@Mapping(source = "fechaNacimiento", target = "fechaNacimiento")
	ResponseActorDTO mapActorToResponseActorDTO(
			final Actor actor);

	@Named(ACTOR_TO_PELICULA_ACTOR_DTO)
	@Mapping(source = "id", target = "id")
	@Mapping(source = "nombreCompleto", target = "nombre")
	@Mapping(source = "fechaNacimiento", target = "fechaNacimiento")
	PeliculaActorDTO mapActorToPeliculaActorDTO(
			final Actor actor);

	List<ResponseActorDTO> mapActorToResponseActorDTO(
			final Collection<Actor> actores);

	//
	// UTILITIES AND CUSTOM MAPPERS
	//

	@Named(EXTRACT_ACTOR_IDS)
	static List<Long> extractActorIds(final List<Actor> source) {
		if (source == null) {
			return null;
		}
		return source.stream()
				.map(Identificable::getId)
				.collect(Collectors.toCollection(LinkedList::new));
	}

}
