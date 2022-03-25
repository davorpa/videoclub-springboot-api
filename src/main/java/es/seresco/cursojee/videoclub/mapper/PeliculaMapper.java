package es.seresco.cursojee.videoclub.mapper;

import java.util.List;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import es.seresco.cursojee.videoclub.business.model.Pelicula;
import es.seresco.cursojee.videoclub.view.dto.pelicula.PeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.RequestActualizarPeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.RequestCrearPeliculaDTO;
import es.seresco.cursojee.videoclub.view.dto.pelicula.ResponsePeliculaDTO;

@Mapper(componentModel = "spring")
public interface PeliculaMapper
{

	//
	// FROM DTO TO ENTITY
	//

	@Mapping(source = "anio", target = "anio")
	@Mapping(source = "codigoGenero", target = "genero.codigo")
	@Mapping(source = "duracion", target = "duracion")
	@Mapping(source = "titulo", target = "titulo")
	@Mapping(target = "actores", ignore = true)
	public Pelicula mapPeliculaDTOToPelicula(
			final PeliculaDTO peliculaDTO);

	@InheritConfiguration(name = "mapPeliculaDTOToPelicula")
	public void mapRequestUpdateDTOToTargetPelicula(
			final RequestActualizarPeliculaDTO requestActualizarPeliculaDTO,
			final @MappingTarget Pelicula pelicula);

	@InheritConfiguration(name = "mapPeliculaDTOToPelicula")
	public Pelicula mapRequestCreateDTOToPelicula(
			final RequestCrearPeliculaDTO requestCrearPeliculaDTO);

	@Mapping(source = "id", target = "id")
	@InheritConfiguration(name = "mapPeliculaDTOToPelicula")
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
	@Mapping(target = "actores", ignore = true)
	public ResponsePeliculaDTO mapPeliculaToResponsePeliculaDTO(
			final Pelicula pelicula);

	public List<ResponsePeliculaDTO> mapPeliculaToResponsePeliculaDTOList(
			final List<Pelicula> peliculas);

}
