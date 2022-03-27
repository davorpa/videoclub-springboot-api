package es.seresco.cursojee.videoclub.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;

import es.seresco.cursojee.videoclub.business.model.Genero;
import es.seresco.cursojee.videoclub.view.dto.genero.GeneroDTO;

@Mapper(componentModel = "spring")
public interface GeneroMapper
{

	//
	// FROM DTO TO ENTITY
	//

	Genero toGenero(
			final GeneroDTO generoDto);

	@IterableMapping(elementTargetType = Genero.class)
	List<Genero> toGeneros(
			final Iterable<GeneroDTO> generoDtos);

	//
	// FROM ENTITY TO DTO
	//

	@InheritInverseConfiguration
	GeneroDTO fromGenero(
			final Genero genero);

	List<GeneroDTO> fromGeneros(
			final Iterable<Genero> generos);

}
