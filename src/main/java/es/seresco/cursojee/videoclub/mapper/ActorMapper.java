package es.seresco.cursojee.videoclub.mapper;

import java.util.List;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import es.seresco.cursojee.videoclub.business.model.Actor;
import es.seresco.cursojee.videoclub.view.dto.actor.ActorDTO;
import es.seresco.cursojee.videoclub.view.dto.actor.RequestActualizarActorDTO;
import es.seresco.cursojee.videoclub.view.dto.actor.RequestCrearActorDTO;
import es.seresco.cursojee.videoclub.view.dto.actor.ResponseActorDTO;

@Mapper(componentModel = "spring")
public interface ActorMapper
{

	//
	// FROM DTO TO ENTITY
	//

	@Mapping(source = "nombre", target = "nombre")
	@Mapping(source = "primerApellido", target = "primerApellido")
	@Mapping(source = "segundoApellido", target = "segundoApellido")
	@Mapping(source = "fechaNacimiento", target = "fechaNacimiento")
	Actor mapActorDTOToActor(
			final ActorDTO actor);

	@InheritConfiguration(name = "mapActorDTOToActor")
	void mapRequestUpdateDTOToTargetActor(
			final RequestActualizarActorDTO requestActualizarActorDTO,
			final @MappingTarget Actor actor);

	@InheritConfiguration(name = "mapActorDTOToActor")
	Actor mapRequestCreateDTOToActor(
			final RequestCrearActorDTO requestCrearActorDTO);

	@Mapping(source = "id", target = "id")
	@InheritConfiguration(name = "mapActorDTOToActor")
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


	List<ResponseActorDTO> mapActorToResponseActorDTOList(
			final List<Actor> actores);

}
