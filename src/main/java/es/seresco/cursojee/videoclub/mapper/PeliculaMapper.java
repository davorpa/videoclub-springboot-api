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

import es.seresco.cursojee.videoclub.business.model.Pelicula;
import es.seresco.cursojee.videoclub.view.dto.Identificable;
import es.seresco.cursojee.videoclub.view.dto.pelicula.PeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.RequestActualizarPeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.RequestCrearPeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.ResponsePeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.ResponseSearchPeliculaDTO;

@Mapper(componentModel = "spring", uses = { ActorMapper.class })
public interface PeliculaMapper
{

	static final String PELICULA_DTO_TO_PELICULA = "peliculaDTOToPelicula";
	static final String EXTRACT_PELICULA_IDS = "extractPeliculaIds";

	//
	// FROM DTO TO ENTITY
	//

	@Named(PELICULA_DTO_TO_PELICULA)
	@Mapping(source = "anio", target = "anio")
	@Mapping(source = "codigoGenero", target = "genero.codigo")
	@Mapping(source = "duracion", target = "duracion")
	@Mapping(source = "titulo", target = "titulo")
	@Mapping(target = "actores", ignore = true)
	public Pelicula peliculaDTOToPelicula(
			final PeliculaDTO peliculaDTO);

	@InheritConfiguration(name = PELICULA_DTO_TO_PELICULA)
	public void updatePeliculaFromDTO(
			final RequestActualizarPeliculaDTO requestActualizarPeliculaDTO,
			final @MappingTarget Pelicula pelicula);

	@InheritConfiguration(name = PELICULA_DTO_TO_PELICULA)
	public Pelicula mapRequestCreateDTOToPelicula(
			final RequestCrearPeliculaDTO requestCrearPeliculaDTO);

	@InheritConfiguration(name = PELICULA_DTO_TO_PELICULA)
	@Mapping(source = "id", target = "id")
	public Pelicula mapRequestUpdateDTOToPelicula(
			final RequestActualizarPeliculaDTO requestActualizarPeliculaDTO);

	//
	// FROM ENTITY TO DTO
	//

	@Mapping(source = "id", target = "id")
	@Mapping(source = "anio", target = "anio")
	@Mapping(source = "genero.codigo", target = "codigoGenero")
	@Mapping(source = "duracion", target = "duracion")
	@Mapping(source = "titulo", target = "titulo")
	@Mapping(source = "actores", target = "actores", qualifiedByName = ActorMapper.EXTRACT_ACTOR_IDS)
	public ResponsePeliculaDTO mapPeliculaToResponsePeliculaDTO(
			final Pelicula pelicula);

	public List<ResponsePeliculaDTO> mapPeliculaToResponsePeliculaDTO(
			final Collection<Pelicula> peliculas);

	@Mapping(source = "id", target = "id")
	@Mapping(source = "anio", target = "anio")
	@Mapping(source = "genero.codigo", target = "codigoGenero")
	@Mapping(source = "duracion", target = "duracion")
	@Mapping(source = "titulo", target = "titulo")
	@Mapping(source = "actores", target = "actores", qualifiedByName = ActorMapper.ACTOR_TO_PELICULA_ACTOR_DTO)
	public ResponseSearchPeliculaDTO mapPeliculaToSearchDTO(
			final Pelicula pelicula);

	public List<ResponseSearchPeliculaDTO> mapPeliculaToSearchDTO(
			final Collection<Pelicula> peliculas);

	//
	// UTILITIES AND CUSTOM MAPPERS
	//

	@Named(EXTRACT_PELICULA_IDS)
	static List<Long> extractPeliculaIds(final List<Pelicula> source) {
		if (source == null) {
			return null;
		}
		return source.stream()
				.map(Identificable::getId)
				.collect(Collectors.toCollection(LinkedList::new));
	}

}
